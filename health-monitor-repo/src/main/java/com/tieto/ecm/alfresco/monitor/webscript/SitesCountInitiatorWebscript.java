package com.tieto.ecm.alfresco.monitor.webscript;

import java.util.HashMap;
import java.util.Map;

import org.alfresco.service.cmr.repository.NodeRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import com.tieto.ecm.alfresco.monitor.service.MonitorJobService;
import com.tieto.ecm.alfresco.monitor.service.job.SitesCountJob;

/**
 * 
 * @author Vitezslav Sliz (vitezslav.sliz@tieto.com)
 */
public class SitesCountInitiatorWebscript extends DeclarativeWebScript{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SitesCountInitiatorWebscript.class);
	
	private SitesCountJob job;
	private MonitorJobService monitorService;
	
	public void setJob(SitesCountJob job) {
		this.job = job;
	}

	public void setMonitorService(MonitorJobService monitorService) {
		this.monitorService = monitorService;
	}
	
	@Override
	protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
		LOGGER.debug("Initiate run calculation");
		
		final NodeRef jobNode = job.createSitesCountJob();
		monitorService.runMonitorOperation(jobNode);

		final Map<String, Object> model = new HashMap<>();
		super.executeImpl(req, status, cache);
		model.put("jobNode", jobNode.toString());
		
		return model;
	}
}
