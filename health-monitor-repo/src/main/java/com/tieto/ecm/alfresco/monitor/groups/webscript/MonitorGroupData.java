package com.tieto.ecm.alfresco.monitor.groups.webscript;

import java.util.Map;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.NamespaceService;
import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import com.tieto.ecm.alfresco.monitor.groups.util.MonitorGroupsUtil;


public class MonitorGroupData extends DeclarativeWebScript {
	private NodeService nodeService;
	private NamespaceService namespaceService;
	private static final Logger logger = Logger.getLogger(MonitorGroupData.class);

	@Override
	protected Map<String, Object> executeImpl(WebScriptRequest req, Status status) {
		
		return MonitorGroupsUtil.getMonitoredNotValidGroups();
	}

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}


	public NamespaceService getNamespaceService() {
		return namespaceService;
	}

	public void setNamespaceService(NamespaceService namespaceService) {
		this.namespaceService = namespaceService;
	}

}
