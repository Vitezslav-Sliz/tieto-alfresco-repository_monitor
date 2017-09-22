package com.tieto.ecm.alfresco.monitor.storage;

import java.io.InputStream;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.model.Repository;
import org.alfresco.repo.node.SystemNodeUtils;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.dictionary.DictionaryService;
import org.alfresco.service.cmr.repository.AssociationRef;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.PermissionService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tieto.ecm.alfresco.monitor.storage.model.MonitorModel;
import com.tieto.ecm.alfresco.monitor.storage.model.JobOperation;
import com.tieto.ecm.alfresco.monitor.storage.model.JobStatus;

/**
 * 
 * @author Vitezslav Sliz (vitezslav.sliz@tieto.com)
 * @version 1.0
 */
public class MonitorStorage {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	// service dependencies
	private Repository repositoryHelper;
	private NodeService nodeService;
	private ContentService contentService;
	private PermissionService permissionService;
	private DictionaryService dictionaryService;
	
	/**
	 * Define name of alfresco container where are stored all monitor requests
	 */
	private String containerName;
	
	private NamespaceService namespaceService;

	public void setRepositoryHelper(Repository repositoryHelper) {
		this.repositoryHelper = repositoryHelper;
	}

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	public void setNamespaceService(NamespaceService namespaceService) {
		this.namespaceService = namespaceService;
	}

	public void setContentService(ContentService contentService) {
		this.contentService = contentService;
	}
	
	public void setPermissionService(PermissionService permissionService) {
		this.permissionService = permissionService;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
	
	public void setDictionaryService(DictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}

	public NodeRef getOrCreateMonitorJobsContainer() {
		return AuthenticationUtil.runAs(new AuthenticationUtil.RunAsWork<NodeRef>() {
			public NodeRef doWork() throws Exception {
		    	final QName containerNameQname = QName.createQName(containerName, namespaceService);
				NodeRef monitorJobsContainer = SystemNodeUtils.getSystemChildContainer(containerNameQname, nodeService, repositoryHelper);
				
				if (monitorJobsContainer == null) {
					LOGGER.info("Lazy creating the Bulk System Container ");
				
					monitorJobsContainer = SystemNodeUtils.getOrCreateSystemChildContainer(containerNameQname, nodeService, repositoryHelper).getFirst();
					permissionService.setPermission(monitorJobsContainer, PermissionService.ALL_AUTHORITIES, PermissionService.CREATE_CHILDREN, true);
				}
				return monitorJobsContainer;
			}
		},AuthenticationUtil.getSystemUserName());
	}
	
	public NodeRef createMonitorJobNode(QName type) {
		final NodeRef bulkContainer = getOrCreateMonitorJobsContainer();

		final Map<QName, Serializable> properties = new HashMap<QName, Serializable>();

		final ChildAssociationRef newChildAssoc = nodeService.createNode(bulkContainer, ContentModel.ASSOC_CHILDREN,ContentModel.ASSOC_CHILDREN, type, properties);

		final NodeRef nodeRef = newChildAssoc.getChildRef();

		// MNT-11911 fix, add ASPECT_INDEX_CONTROL and property that not create
		// indexes for search and not visible files/folders at 'My Documents'
		// dashlet
		final Map<QName, Serializable> aspectProperties = new HashMap<QName, Serializable>(2);
		aspectProperties.put(ContentModel.PROP_IS_INDEXED, Boolean.FALSE);
		aspectProperties.put(ContentModel.PROP_IS_CONTENT_INDEXED, Boolean.FALSE);
		nodeService.addAspect(nodeRef, ContentModel.ASPECT_INDEX_CONTROL, aspectProperties);

		LOGGER.debug("Created monitor job node. Bulk-NodeRef={} of type={}",nodeRef,type);
		
		return nodeRef;
	}

	public void addSourcePathNode(final NodeRef monitorNodeRef, final NodeRef sourceParentNode) {
		validateMonitorNode(monitorNodeRef, MonitorModel.TYPE_MONITOR_NODES_HIERARCHY);
		nodeService.createAssociation(monitorNodeRef, sourceParentNode, MonitorModel.ASSOC_SOURCE_NODE_PATH);

		LOGGER.debug("Source root path node added to Monitor-NodeRef '{}'. PathNode={}",monitorNodeRef, sourceParentNode);
	}
	
	public void addDepth(final NodeRef monitorNodeRef, long depth) {
		validateMonitorNode(monitorNodeRef, MonitorModel.TYPE_MONITOR_ACL_HIERARCHY);
		nodeService.setProperty(monitorNodeRef, MonitorModel.PROP_ACL_DEPTH, depth);
		LOGGER.debug("Depth count added to Monitor-NodeRef '{}'. PathNode={}",monitorNodeRef, depth);
	}
	
	public void setMonitorData(final NodeRef monitorNodeRef,final InputStream monitorData) {
		final ContentWriter writer = contentService.getWriter(monitorNodeRef, ContentModel.PROP_CONTENT, true);
		writer.putContent(monitorData);
		LOGGER.debug("Monitor data added to Monitor-NodeRef '{}'.",monitorNodeRef);
	}

	public NodesHierarchyMonitorRequest getNodesHierarchyMonitorRequest(final NodeRef monitorNodeRef) {
		validateMonitorNode(monitorNodeRef,  MonitorModel.TYPE_MONITOR_NODES_HIERARCHY);

		NodeRef sourcePath = null;
		List<AssociationRef> requestedSourceNode = nodeService.getTargetAssocs(monitorNodeRef,MonitorModel.ASSOC_SOURCE_NODE_PATH);
		for (AssociationRef asoc : requestedSourceNode){ 
			sourcePath = asoc.getTargetRef();
			break;
        }
		
		return new NodesHierarchyMonitorRequest(sourcePath);
	}
	
	public AclHierarchyMonitorRequest getAclRequest(final NodeRef monitorNodeRef) {
		validateMonitorNode(monitorNodeRef,  MonitorModel.TYPE_MONITOR_ACL_HIERARCHY);
		
		final Long depth = (Long)nodeService.getProperty(monitorNodeRef, MonitorModel.PROP_ACL_DEPTH);
        
		return new AclHierarchyMonitorRequest(depth);
	}

	/**
	 * Get POJO object which represent Monitor request status
	 * @param monitorNodeRef Monitor nodeRef
	 * @return MonitorStatus POJO object
	 */
	public JobStatus getMonitorStatus(final NodeRef monitorNodeRef) {
		validateMonitorNode(monitorNodeRef,MonitorModel.TYPE_MONITOR);
		final Map<QName, Serializable> properties = nodeService.getProperties(monitorNodeRef);

		final String status = (String) properties.get(MonitorModel.PROP_STATUS);
		final String message = (String) properties.get(MonitorModel.PROP_MESSAGE);

		LOGGER.debug("Getting status for node {} paramms: status = {}, message = {}", monitorNodeRef, status, message);
		return new JobStatus(JobStatus.Status.valueOf(status),message);
	}

	/**
	 * Update actual status of monitor request
	 * 
	 * @param nodeRef Monitor nodeRef
	 * @param status MonitorStatus POJO
	 */
	public void updateStatus(final NodeRef nodeRef, final JobStatus status) {
		validateMonitorNode(nodeRef,MonitorModel.TYPE_MONITOR);

		nodeService.setProperty(nodeRef, MonitorModel.PROP_STATUS, status.getStatus().toString());
		nodeService.setProperty(nodeRef, MonitorModel.PROP_MESSAGE, status.getMessage());
	}
	
	public void setErrorStatus(final NodeRef nodeRef) {
		validateMonitorNode(nodeRef,MonitorModel.TYPE_MONITOR);
		nodeService.setProperty(nodeRef, MonitorModel.PROP_STATUS,JobStatus.Status.ERROR.toString());
	}
	
	private void validateMonitorNode(final NodeRef monitorNodeRef, final QName type) {
		if (!dictionaryService.isSubClass(nodeService.getType(monitorNodeRef), type)) {
			throw new IllegalArgumentException("Invalid node type for nodeRef:-" + monitorNodeRef);
		}
	}

	public JobOperation getMonitorOperation(NodeRef monitorNodeRef) {
		validateMonitorNode(monitorNodeRef,MonitorModel.TYPE_MONITOR);
		final String oper = (String)nodeService.getProperty(monitorNodeRef, MonitorModel.PROP_OPERATION);
		return JobOperation.valueOf(oper.toUpperCase());
	}

	public void setOperation(NodeRef monitorNode, JobOperation operation) {
		validateMonitorNode(monitorNode,MonitorModel.TYPE_MONITOR);
		nodeService.setProperty(monitorNode, MonitorModel.PROP_OPERATION, operation);
		
	}
}