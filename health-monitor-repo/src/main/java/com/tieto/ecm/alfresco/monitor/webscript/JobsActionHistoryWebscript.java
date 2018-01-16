package com.tieto.ecm.alfresco.monitor.webscript;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import com.tieto.ecm.alfresco.monitor.service.MonitorSearchService;
import com.tieto.ecm.alfresco.monitor.service.pojo.JobHistory;
import com.tieto.ecm.alfresco.monitor.util.NumberUtils;

/**
 * 
 * @author Tomas Privoznik
 * @author D. Katanik
 */
public class JobsActionHistoryWebscript extends DeclarativeWebScript {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobsActionHistoryWebscript.class);

	private String limit;

	private MonitorSearchService monitorSearchService;
	
	public void setLimit(String limit) {
		this.limit = limit;
	}
	
	public void setMonitorSearchService(MonitorSearchService monitorSearchService) {
		this.monitorSearchService = monitorSearchService;
	}

	@Override
	protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
		LOGGER.debug("Get Monitor jobs");

		// get parameters
		final Integer limitNumberOfNodesParam = NumberUtils.acquireIntParameter(req.getParameter("limit"));
		final Integer limitNumberOfNodes = limitNumberOfNodesParam!=null?limitNumberOfNodesParam:Integer.valueOf(limit);
		String jobOperation = req.getParameter("jobOperation");
		String jobStatus = req.getParameter("jobStatus");

		final List<JobHistory> historicalJobs = monitorSearchService.getJobsHistory(limitNumberOfNodes, jobOperation, jobStatus);

		final Map<String, Object> model = new HashMap<>();
		model.put("jobs", historicalJobs);
		model.put("limit", limitNumberOfNodes);
		model.put("jobOperation", jobOperation);
		model.put("jobStatus", jobStatus);

		return model;
	}
}
