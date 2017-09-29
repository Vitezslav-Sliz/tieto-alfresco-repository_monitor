package com.tieto.ecm.alfresco.monitor.webscript;

import java.util.HashMap;
import java.util.Map;

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
	private String depth;
	private AclHierarchyJob job;
	private MonitorJobService monitorService;

	private static final Logger LOGGER = Logger.getLogger(MonitorAclHierarchyWebscript.class);

	@Override
	protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
		LOGGER.debug("Initiate run calculation");
		
		long requestDepth = acquireLongParameter(req, "depth");
		final NodeRef jobNode = job.createAclHierarchyJob(requestDepth);

		monitorService.runMonitorOperation(jobNode);
		final Map<String, Object> model = new HashMap<>();
		model.put("jobNode", jobNode.toString());
		model.put("depth", String.valueOf(requestDepth));
		
		return model;
	}
	
	private long acquireLongParameter(WebScriptRequest req, String parameterName) {
		String parameter = req.getParameter(parameterName);
		Long result;
		
		try {
			result = Long.parseLong(parameter);
		} catch (Exception e) {
			LOGGER.debug("Failed to acquire parameter: " + parameterName);
			result = Long.parseLong(depth);
		}
		
		return result;
	}

	public void setJob(AclHierarchyJob job) {
		this.job = job;
	}

	public void setMonitorService(MonitorJobService monitorService) {
		this.monitorService = monitorService;
	}

	public void setDepth(String depth) {
		this.depth = depth;
	}
}
