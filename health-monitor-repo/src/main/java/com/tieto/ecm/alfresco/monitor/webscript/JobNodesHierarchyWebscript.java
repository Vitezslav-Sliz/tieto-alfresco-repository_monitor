package com.tieto.ecm.alfresco.monitor.webscript;

import java.util.HashMap;
import java.util.Map;

import org.alfresco.repo.model.Repository;
import org.alfresco.service.cmr.repository.NodeRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import com.tieto.ecm.alfresco.monitor.service.MonitorJobService;
import com.tieto.ecm.alfresco.monitor.service.job.NodesHierarchyJob;

public class JobNodesHierarchyWebscript extends DeclarativeWebScript{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JobNodesHierarchyWebscript.class);
	
	private NodesHierarchyJob job;
	private MonitorJobService monitorService;
	private Repository repositoryHelper;
	
	public void setJob(NodesHierarchyJob job) {
		this.job = job;
	}

	public void setMonitorService(MonitorJobService monitorService) {
		this.monitorService = monitorService;
	}

	public void setRepositoryHelper(Repository repositoryHelper) {
		this.repositoryHelper = repositoryHelper;
	}

	@Override
	protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
		LOGGER.debug("Initiate run calculation");
		final NodeRef jobNode = job.createNodesHierarchyJob(repositoryHelper.getCompanyHome());
		monitorService.runMonitorOperation(jobNode);
		
		final Map<String, Object> model = new HashMap<>();
		model.put("jobNode", jobNode.toString());
		return super.executeImpl(req, status, cache);
	}
}
