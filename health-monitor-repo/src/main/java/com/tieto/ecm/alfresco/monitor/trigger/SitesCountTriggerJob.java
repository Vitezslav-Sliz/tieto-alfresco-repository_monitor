package com.tieto.ecm.alfresco.monitor.trigger;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.schedule.AbstractScheduledLockedJob;
import org.alfresco.service.cmr.repository.NodeRef;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import com.tieto.ecm.alfresco.monitor.service.MonitorJobService;
import com.tieto.ecm.alfresco.monitor.service.job.SitesCountJob;

public class SitesCountTriggerJob extends AbstractScheduledLockedJob implements StatefulJob {

	@Override
	public void executeJob(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobData = context.getJobDetail().getJobDataMap();

		// Extract the Job executer to use
		Object executerObj = jobData.get("jobExecuter");
		if (executerObj == null || !(executerObj instanceof MonitorJobService)) {
			throw new AlfrescoRuntimeException("SitesCountJob data must contain valid 'Executer' reference");
		}
		
		Object executerCreateObj = jobData.get("jobCreateExecuter");
		if (executerCreateObj == null || !(executerCreateObj instanceof SitesCountJob)) {
			throw new AlfrescoRuntimeException("SitesCountJob data must contain valid 'CreateExecuter' reference");
		}
		final SitesCountJob jobCreateExecuter = (SitesCountJob) executerCreateObj;
		final MonitorJobService jobExecuter = (MonitorJobService) executerObj;

		AuthenticationUtil.runAs(new AuthenticationUtil.RunAsWork<Object>() {
			public Object doWork() throws Exception {
				final NodeRef jobNode = jobCreateExecuter.createSitesCountJob();
				jobExecuter.runMonitorOperation(jobNode);
				return null;
			}
		}, AuthenticationUtil.getSystemUserName());
	}
}