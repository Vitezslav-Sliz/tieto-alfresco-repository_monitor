package com.tieto.ecm.alfresco.monitor.trigger;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.transaction.RetryingTransactionHelper;
import org.alfresco.repo.transaction.RetryingTransactionHelper.RetryingTransactionCallback;
import org.alfresco.schedule.AbstractScheduledLockedJob;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import com.tieto.ecm.alfresco.monitor.service.MonitorJobService;
import com.tieto.ecm.alfresco.monitor.service.job.NodesHierarchyJob;

public class NodesHierarchyTriggerJob extends AbstractScheduledLockedJob implements StatefulJob {

	@Override
	public void executeJob(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobData = context.getJobDetail().getJobDataMap();

		// Extract the Job executer to use
		Object executerObj = jobData.get("jobExecuter");
		if (executerObj == null || !(executerObj instanceof MonitorJobService)) {
			throw new AlfrescoRuntimeException("Job data must contain valid 'Executer' reference");
		}

		Object executerCreateObj = jobData.get("jobCreateExecuter");
		if (executerCreateObj == null || !(executerCreateObj instanceof NodesHierarchyJob)) {
			throw new AlfrescoRuntimeException("SitesCountJob data must contain valid 'CreateExecuter' reference");
		}
		Object executerTransactHelper = jobData.get("jobTransactionHelper");
		if (executerTransactHelper == null || !(executerTransactHelper instanceof RetryingTransactionHelper)) {
			throw new AlfrescoRuntimeException(
					"jobTransactionHelper data must contain valid 'Transaction helper' reference");
		}
		final NodesHierarchyJob jobCreateExecuter = (NodesHierarchyJob) executerCreateObj;
		final MonitorJobService jobExecuter = (MonitorJobService) executerObj;
		final RetryingTransactionHelper transactionHelper = (RetryingTransactionHelper) executerTransactHelper;
		AuthenticationUtil.runAs(new AuthenticationUtil.RunAsWork<Object>() {

			public Object doWork() throws Exception {
				return transactionHelper.doInTransaction(new RetryingTransactionCallback<NodeRef>() {
					@Override
					public NodeRef execute() throws Throwable {
						final NodeRef jobNode = jobCreateExecuter.createTriggerJob();
						jobExecuter.runMonitorOperation(jobNode);
						return null;
					}
				}, false, true);
			}
		}, AuthenticationUtil.getSystemUserName());
	}

}