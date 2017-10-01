package com.tieto.ecm.alfresco.monitor.webscript;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
		int numberOfNodes = acquireIntParameter(req, "limit");
		
		List<JobHistory> result = new ArrayList<>(numberOfNodes);
		
		NodeRef node = monitorStorage.getOrCreateMonitorJobsContainer();
		List<ChildAssociationRef> childAssocs = nodeService.getChildAssocs(node);
		Collections.reverse(childAssocs);
		
		for (ChildAssociationRef child : childAssocs.stream().limit(numberOfNodes).collect(Collectors.toList())) {
			JobHistory job = new JobHistory();
			job.type = (String) nodeService.getProperty(child.getChildRef(), MonitorModel.PROP_OPERATION);
			job.status = (String) nodeService.getProperty(child.getChildRef(), MonitorModel.PROP_STATUS);
			job.nodeRef = child.getChildRef().toString();
			result.add(job);
		}

		final Map<String, Object> model = new HashMap<>();
		model.put("jobs", result);
		
		return model;
	}
	
	public class JobHistory {
		String type;
		String nodeRef;
		String status;
		
		public String getNodeRef() {
			return nodeRef;
		}
		
		public String getStatus() {
			return status;
		}
		
		public String getType() {
			return type;
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
