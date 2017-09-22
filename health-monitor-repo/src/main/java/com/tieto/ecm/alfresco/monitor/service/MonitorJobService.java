package com.tieto.ecm.alfresco.monitor.service;

import org.alfresco.service.cmr.repository.NodeRef;

import com.tieto.ecm.alfresco.monitor.storage.model.JobOperation;
import com.tieto.ecm.alfresco.monitor.storage.model.JobStatus;

/**
 * 
 * @author Vitezslav Sliz (vitezslav.sliz@tieto.com)
 * @version 1.0
 */
public interface MonitorJobService {
	
	public JobStatus getMonitorStatus(final NodeRef monitorNodeRef);
	
	public JobOperation getMonitorOperation(final NodeRef monitorNodeRef);

	/**
	 * Initiate processing of monitor request 
	 * @param monitorNodeRef
	 */
	public void runMonitorOperation(final NodeRef monitorNodeRef);
	
}
