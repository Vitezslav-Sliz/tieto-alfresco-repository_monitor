package com.tieto.ecm.alfresco.monitor.service.job;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.repo.processor.BaseProcessorExtension;
import org.alfresco.repo.transaction.RetryingTransactionHelper;
import org.alfresco.repo.transaction.RetryingTransactionHelper.RetryingTransactionCallback;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.util.ParameterCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tieto.ecm.alfresco.monitor.storage.MonitorStorage;
import com.tieto.ecm.alfresco.monitor.storage.model.JobOperation;
import com.tieto.ecm.alfresco.monitor.storage.model.MonitorModel;
import com.tieto.ecm.alfresco.monitor.webscript.JobNodesHierarchyWebscript;

public class NodesHierarchyJobImpl extends BaseProcessorExtension implements NodesHierarchyJob {

	private RetryingTransactionHelper transactionHelper;
	private MonitorStorage monitorStorage;
	private SearchService searchService;
	private String hierarchyDepth;
	private String numberOfNodes;
	private String sourcePathName;
	private static final Logger LOGGER = LoggerFactory.getLogger(NodesHierarchyJobImpl.class);

	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

	public void setHierarchyDepth(String hierarchyDepth) {
		this.hierarchyDepth = hierarchyDepth;
	}

	public void setNumberOfNodes(String numberOfNodes) {
		this.numberOfNodes = numberOfNodes;
	}

	public void setSourcePathName(String sourcePathName) {
		this.sourcePathName = sourcePathName;
	}

	public void setTransactionHelper(RetryingTransactionHelper transactionHelper) {
		this.transactionHelper = transactionHelper;
	}

	public void setMonitorStorage(MonitorStorage monitorStorage) {
		this.monitorStorage = monitorStorage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tieto.ecm.alfresco.monitor.service.job.NodesHierarchyJob#
	 * createNodesHuerarchyJob(org.alfresco.service.cmr.repository.NodeRef)
	 */
	@Override
	public NodeRef createNodesHierarchyJob(final NodeRef sourcePathNodeRef, int numberOfNodes, int hierarchyDepth) {
		ParameterCheck.mandatory("sourcePathNodeRef", sourcePathNodeRef);
		return transactionHelper.doInTransaction(new RetryingTransactionCallback<NodeRef>() {
			@Override
			public NodeRef execute() throws Throwable {
				// Create a download node
				final NodeRef monitorNode = monitorStorage
						.createMonitorJobNode(MonitorModel.TYPE_MONITOR_NODES_HIERARCHY);
				monitorStorage.setOperation(monitorNode, JobOperation.NODES_HIERARCHY);
				// Add requested nodes
				if (sourcePathNodeRef != null) {
					monitorStorage.addSourcePathNode(monitorNode, sourcePathNodeRef);
				}
				monitorStorage.addHierarchyDepth(monitorNode, hierarchyDepth);
				monitorStorage.addNumberOfChildren(monitorNode, numberOfNodes);

				return monitorNode;
			}
		}, false, true);
	}

	public NodeRef getNodeFromPathName(String pathName) {
		StoreRef storeRef = new StoreRef(StoreRef.PROTOCOL_WORKSPACE, "SpacesStore");
		ResultSet rs = searchService.query(storeRef, SearchService.LANGUAGE_XPATH, String.format("/app:%s", pathName));

		NodeRef sourceNameNodeRef = null;
		try {
			if (rs.length() == 0) {
				throw new AlfrescoRuntimeException(String.format("Didn't find %s", pathName));
			}
			sourceNameNodeRef = rs.getNodeRef(0);
		} finally {
			rs.close();
		}
		return sourceNameNodeRef;
	}

	@Override
	public NodeRef createTriggerJob() {
		NodeRef sourcePathNodeRef = getNodeFromPathName(sourcePathName);
		int nOfN = Integer.valueOf(numberOfNodes);
		int hDepth = Integer.valueOf(hierarchyDepth);
		LOGGER.debug("sourcePathNoderef {},nOfN {},hDepth {}",new Object[] {sourcePathNodeRef, nOfN, hDepth} );
		return createNodesHierarchyJob(sourcePathNodeRef, nOfN, hDepth);
	}
}
