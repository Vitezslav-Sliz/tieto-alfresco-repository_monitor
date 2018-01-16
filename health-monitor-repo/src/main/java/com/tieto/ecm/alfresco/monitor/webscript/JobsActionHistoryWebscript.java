package com.tieto.ecm.alfresco.monitor.webscript;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import com.tieto.ecm.alfresco.monitor.storage.MonitorStorage;
import com.tieto.ecm.alfresco.monitor.storage.model.JobOperation;
import com.tieto.ecm.alfresco.monitor.storage.model.JobStatus;
import com.tieto.ecm.alfresco.monitor.storage.model.MonitorModel;

/**
 * 
 * @author Tomas Privoznik
 * @author D. Katanik
 */
public class JobsActionHistoryWebscript extends DeclarativeWebScript {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobsActionHistoryWebscript.class);

	private String limit;

	private NodeService nodeService;
	private MonitorStorage monitorStorage;

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	public void setMonitorStorage(MonitorStorage monitorStorage) {
		this.monitorStorage = monitorStorage;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	@Override
	protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
		LOGGER.debug("Initiate run calculation");

		// get parameters
		int limitNumberOfNodes = acquireIntParameter(req, "limit");
		String jobOperation = req.getParameter("jobOperation");
		String jobStatus = req.getParameter("jobStatus");

		List<JobHistory> historicalJobs = getJobsHistory(limitNumberOfNodes, jobOperation, jobStatus);

		final Map<String, Object> model = new HashMap<>();
		model.put("jobs", historicalJobs);
		model.put("limit", limitNumberOfNodes);
		model.put("jobOperation", jobOperation);
		model.put("jobStatus", jobStatus);

		return model;
	}

	/**
	 * Get historical jobs
	 * 
	 * @param numberOfNodes
	 *            how many nodes should be retrieve
	 * @param operation
	 *            which job should be retrieve depends on{@link JobOperation}. Could be null.
	 * @param status
	 *            which job should be retrieve depends on {@link JobStatus}. Could be null.
	 * @return List of jobs in history
	 */
	private List<JobHistory> getJobsHistory(int numberOfNodes, String operation, String status) {
		List<JobHistory> result = new ArrayList<>(numberOfNodes);

		NodeRef node = monitorStorage.getOrCreateMonitorJobsContainer();
		List<ChildAssociationRef> childAssocs = nodeService.getChildAssocs(node);
		Collections.reverse(childAssocs);

		Stream<ChildAssociationRef> streamChildrenAssocs = childAssocs.stream();
		
		if (operation != null && !operation.isEmpty()) {
			streamChildrenAssocs = streamChildrenAssocs.filter(ch -> ((String) nodeService.getProperty(ch.getChildRef(), MonitorModel.PROP_OPERATION)).equalsIgnoreCase(operation));
		}
		if (status != null && !status.isEmpty()) {
			streamChildrenAssocs = streamChildrenAssocs.filter(ch -> ((String) nodeService.getProperty(ch.getChildRef(), MonitorModel.PROP_STATUS)).equalsIgnoreCase(status));
		}

		childAssocs = streamChildrenAssocs.limit(numberOfNodes).collect(Collectors.toList());

		for (ChildAssociationRef child : childAssocs) {
			JobHistory job = new JobHistory();
			job.operation = operation;
			job.status = (String) nodeService.getProperty(child.getChildRef(), MonitorModel.PROP_STATUS);
			job.nodeRef = child.getChildRef().toString();
			job.date = (Date) nodeService.getProperty(child.getChildRef(), ContentModel.PROP_CREATED);
			result.add(job);
		}
		return result;
	}

	public class JobHistory {
		String operation;
		String nodeRef;
		String status;
		Date date;

		private JobHistory() {};
		
		public String getNodeRef() {
			return nodeRef;
		}

		public String getStatus() {
			return status;
		}

		public String getType() {
			return operation;
		}
		
		public Date getDate() {
			return date;
		}
	}

	private int acquireIntParameter(WebScriptRequest req, String parameterName) {
		String parameter = req.getParameter(parameterName);
		try {
			return Integer.parseInt(parameter);
		} catch (Exception e) {
			LOGGER.debug("Failed to acquire parameter: " + parameterName);
			return Integer.parseInt(limit);
		}
	}
}
