package com.tieto.ecm.alfresco.monitor.service.processor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.model.Repository;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.util.Pair;
import org.alfresco.util.ParameterCheck;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tieto.ecm.alfresco.monitor.storage.model.JobStatus;
import com.tieto.ecm.alfresco.monitor.storage.model.JobStatus.Status;
import com.tieto.ecm.alfresco.monitor.storage.model.MonitorModel;

/**
 * Class implements processor which searches nodes violating hierarchy depth
 * policy or policy for maximum number of children
 * 
 * @author Tomas Privoznik
 */
public class MonitorNodesHierarchyAction extends AbstractMonitorExecuterAction {

	private static final Logger LOGGER = LoggerFactory.getLogger(MonitorNodesHierarchyAction.class);

	private JSONArray countNodes;
	private JSONArray depthNodes;

	private int hierarchyCount;
	private int childrenCount;

	private long numberOfNodesParam;
	private long hierarchyDepthParam;

	private NodeService nodeService;
	private Repository repositoryHelper;

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	public void setRepositoryHelper(Repository repositoryHelper) {
		this.repositoryHelper = repositoryHelper;
	}

	@Override
	protected void executeImpl(NodeRef actionedUponNodeRef) {
		LOGGER.debug("Start node hierarchy job");

		// delete old data when job is executed
		clearFields();

		// save parameters from action acquire
		acquireNodesProperties(actionedUponNodeRef);

		try {
			// perform search to check number of children and hierarchy depth
			searchNodes(repositoryHelper.getCompanyHome(), new ArrayList<Pair<String, NodeRef>>());

			// save data as content to job node
			monitorStorage.setMonitorData(actionedUponNodeRef,
					IOUtils.toInputStream(prepareDataToJSON().toString(), "UTF-8"));
			monitorStorage.updateStatus(actionedUponNodeRef, new JobStatus(Status.FINISHED, "FINISHED"));
		} catch (Exception e) {
			LOGGER.debug("Node hierarchy job faild. NodeRef: " + actionedUponNodeRef.toString());
			monitorStorage.updateStatus(actionedUponNodeRef, new JobStatus(Status.ERROR, e.getMessage()));
		}
	}

	/**
	 * Find nodes which violates hierarchy depth policy or maximum number of
	 * children policy. DFS search starts from company home.
	 * 
	 * @param nodeRef
	 *            - current node in search
	 * @param nodesHierarchy
	 *            - path from company home to current node
	 * @throws Exception
	 *             - set job status to error
	 */
	private void searchNodes(NodeRef nodeRef, List<Pair<String, NodeRef>> nodesHierarchy) throws Exception {
		String nodesName = (String) nodeService.getProperty(nodeRef, ContentModel.PROP_NAME);
		nodesHierarchy.add(new Pair<String, NodeRef>(nodesName, nodeRef));

		// check hierarchy depth
		if (isHierarchyInRange(nodesHierarchy, hierarchyDepthParam)) {
			hierarchyCount++;
			// save node
			saveNodeAndPath(nodesHierarchy, depthNodes, nodesHierarchy.size());
		}

		// get children
		List<ChildAssociationRef> childrenOfNode = nodeService.getChildAssocs(nodeRef);

		// check number of nodes
		if (isHierarchyInRange(childrenOfNode, numberOfNodesParam)) {
			childrenCount++;
			// save node
			saveNodeAndPath(nodesHierarchy, countNodes, childrenOfNode.size());
		}

		// go through all child associations of node
		for (ChildAssociationRef assoc : childrenOfNode) {
			searchNodes(assoc.getChildRef(), new ArrayList<Pair<String, NodeRef>>(nodesHierarchy));
		}
	}

	private void saveNodeAndPath(List<Pair<String, NodeRef>> nodesHierarchy, JSONArray nodes,
			long count) throws JSONException {
		StringBuilder pathBuilder = new StringBuilder();
		NodeRef nodeRef = null;

		for (int i = 0; i < nodesHierarchy.size(); i++) {
			pathBuilder.append("/").append(nodesHierarchy.get(i).getFirst());
			nodeRef = nodesHierarchy.get(i).getSecond();
		}
		saveNodeToJSONArray(nodeRef.toString(), pathBuilder.toString(), nodes, count);
	}

	private <T> boolean isHierarchyInRange(List<T> nodesHierarchy, long size) throws NullPointerException {
		return nodesHierarchy.size() > size;
	}

	private void saveNodeToJSONArray(String nodeRef, String path, JSONArray nodes, long count) throws JSONException {
		ParameterCheck.mandatoryString("NodeRef", nodeRef);
		ParameterCheck.mandatoryString("Path", path);

		JSONObject tempObj = new JSONObject();

		tempObj.put("nodeRef", nodeRef.toString());
		tempObj.put("path", path);
		tempObj.put("nodeCount", count);

		nodes.put(tempObj);
	}

	private JSONObject prepareDataToJSON() throws JSONException {
		JSONObject result = new JSONObject();
		JSONObject subJsonObject = new JSONObject();

		subJsonObject.put("count", countNodes);
		subJsonObject.put("depth", depthNodes);
		result.put("data", subJsonObject);
		result.put("numberOfNodeParameter", numberOfNodesParam);
		result.put("nodesHierarchyParameter", hierarchyDepthParam);
		result.put("nodesHierarchyCount", hierarchyCount);
		result.put("numberOfChildrenCount", childrenCount);
		result.put("server", "host");
		result.put("time", LocalDateTime.now().toString());

		return result;
	}

	private void acquireNodesProperties(NodeRef actionedUponNodeRef) {
		this.numberOfNodesParam = (long) nodeService.getProperty(actionedUponNodeRef,
				MonitorModel.PROP_NUMBER_OF_CHILDREN);
		this.hierarchyDepthParam = (long) nodeService.getProperty(actionedUponNodeRef,
				MonitorModel.PROP_HIERARCHY_DEPTH);
	}

	private void clearFields() {
		countNodes = new JSONArray();
		depthNodes = new JSONArray();
		hierarchyCount = 0;
		childrenCount = 0;
	}
}