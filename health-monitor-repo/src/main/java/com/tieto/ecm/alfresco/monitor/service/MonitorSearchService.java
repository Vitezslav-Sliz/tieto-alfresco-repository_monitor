package com.tieto.ecm.alfresco.monitor.service;

import java.util.List;

import com.tieto.ecm.alfresco.monitor.service.pojo.JobHistory;
import com.tieto.ecm.alfresco.monitor.storage.model.JobOperation;
import com.tieto.ecm.alfresco.monitor.storage.model.JobStatus;

public interface MonitorSearchService {


	/**
	 * Get historical jobs
	 * 
	 * @param count
	 *            how many nodes should be retrieve
	 * @param operation
	 *            which job should be retrieve depends on{@link JobOperation}. Could be null.
	 * @param status
	 *            which job should be retrieve depends on {@link JobStatus}. Could be null.
	 * @return List of jobs in history
	 */
	public List<JobHistory> getJobsHistory(final int count,final String operation,final String status);
}
