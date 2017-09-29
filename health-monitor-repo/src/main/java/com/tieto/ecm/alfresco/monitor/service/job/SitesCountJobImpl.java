package com.tieto.ecm.alfresco.monitor.service.job;

import org.alfresco.repo.processor.BaseProcessorExtension;
import org.alfresco.repo.transaction.RetryingTransactionHelper;
import org.alfresco.repo.transaction.RetryingTransactionHelper.RetryingTransactionCallback;
import org.alfresco.service.cmr.repository.NodeRef;

import com.tieto.ecm.alfresco.monitor.storage.MonitorStorage;
import com.tieto.ecm.alfresco.monitor.storage.model.JobOperation;
import com.tieto.ecm.alfresco.monitor.storage.model.MonitorModel;

/**
 * Implement class which creates node which represent job for site count.
 * 
 * @author Vitezslav Sliz (vitezslav.sliz@tieto.com)
 * @version 1.0
 */
public class SitesCountJobImpl extends BaseProcessorExtension implements SitesCountJob {

	private RetryingTransactionHelper transactionHelper;
	private MonitorStorage monitorStorage;

	public void setTransactionHelper(RetryingTransactionHelper transactionHelper) {
		this.transactionHelper = transactionHelper;
	}

	public void setMonitorStorage(MonitorStorage monitorStorage) {
		this.monitorStorage = monitorStorage;
	}
	
	@Override
	public NodeRef createSitesCountJob() {
		return transactionHelper.doInTransaction(new RetryingTransactionCallback<NodeRef>() {
            @Override
            public NodeRef execute() throws Throwable {
                //Create a site count node
                final NodeRef monitorNode = monitorStorage.createMonitorJobNode(MonitorModel.TYPE_MONITOR_SITES_COUNT);
                monitorStorage.setOperation(monitorNode, JobOperation.SITES_COUNT);
                return monitorNode;
            }
        }, false, true);
	}
}
