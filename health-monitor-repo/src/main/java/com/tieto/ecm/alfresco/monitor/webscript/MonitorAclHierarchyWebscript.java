package com.tieto.ecm.alfresco.monitor.webscript;

import java.util.HashMap;
import java.util.Map;

import org.alfresco.repo.model.Repository;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import com.tieto.ecm.alfresco.monitor.service.MonitorJobService;
import com.tieto.ecm.alfresco.monitor.service.job.AclHierarchyJob;

/**
 * 
 * @author D.Katanik
 *
 */
public class MonitorAclHierarchyWebscript extends DeclarativeWebScript {
	// Should be in global props or in request?
	private static final int DEEP = 5;
	private AclHierarchyJob job;
	private MonitorJobService monitorService;

	private static final Logger LOGGER = Logger.getLogger(MonitorAclHierarchyWebscript.class);

	@Override
	protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
		LOGGER.debug("Initiate run calculation");
		final NodeRef jobNode = job.createAclHierarchyJob(DEEP);
		
		monitorService.runMonitorOperation(jobNode);
		final Map<String, Object> model = new HashMap<>();
		model.put("jobNode", jobNode.toString());
		
		return model;
	}

	public void setJob(AclHierarchyJob job) {
		this.job = job;
	}

	public void setMonitorService(MonitorJobService monitorService) {
		this.monitorService = monitorService;
	}
}
