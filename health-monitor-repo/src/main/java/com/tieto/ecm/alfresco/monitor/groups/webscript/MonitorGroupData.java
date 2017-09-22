package com.tieto.ecm.alfresco.monitor.groups.webscript;

import java.util.Map;
import java.util.Set;

import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.NamespaceService;
import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import com.tieto.ecm.alfresco.monitor.groups.service.MonitorGroupsService;
import com.tieto.ecm.alfresco.monitor.groups.util.MonitorGroupsUtil;


public class MonitorGroupData extends DeclarativeWebScript {
	private NodeService nodeService;
	private NamespaceService namespaceService;
	private MonitorGroupsService monitorGroupsService;
	private static final Logger logger = Logger.getLogger(MonitorGroupData.class);

	@Override
	protected Map<String, Object> executeImpl(WebScriptRequest req, Status status) {
		Map<String, Set<String>> executeImpl = monitorGroupsService.executeImpl();
		
		
		
		return MonitorGroupsUtil.getMonitoredNotValidGroups(executeImpl.keySet(), 5);
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

	public void setMonitorGroupsService(MonitorGroupsService monitorGroupsService) {
		this.monitorGroupsService = monitorGroupsService;
	}

}
