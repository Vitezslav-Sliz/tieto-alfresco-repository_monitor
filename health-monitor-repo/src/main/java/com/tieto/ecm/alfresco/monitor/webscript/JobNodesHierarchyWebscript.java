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

/**
 * 
 * @author Vitezslav Sliz (vitezslav.sliz@tieto.com)
 * @author Privoznik Tomas
 */
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
		
		// get parameters
		int numberOfNodes = acquireIntParameter(req, "numberOfNodes");
		int hierarchyDepth = acquireIntParameter(req, "hierarchyDepth");
		
		// TODO: change company home to some custom node 
		final NodeRef jobNode = job.createNodesHierarchyJob(repositoryHelper.getCompanyHome(), numberOfNodes, hierarchyDepth);
		monitorService.runMonitorOperation(jobNode);

		final Map<String, Object> model = new HashMap<>();
		super.executeImpl(req, status, cache);
		model.put("jobNode", jobNode.toString());
		model.put("numberOfNodes", numberOfNodes);
		model.put("hierarchyDepth", hierarchyDepth);
		
		return model;
	}
	
	private int acquireIntParameter(WebScriptRequest req, String parameterName) {
		String parameter = req.getParameter(parameterName);
		try {
			return Integer.parseInt(parameter);
		} catch (Exception e) {
			LOGGER.debug("Failed to acquire parameter: " + parameterName);
			return 15;
		}
	}
}
