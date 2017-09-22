package com.tieto.ecm.alfresco.monitor.service.job;

import org.alfresco.repo.processor.BaseProcessorExtension;
import org.alfresco.repo.transaction.RetryingTransactionHelper;
import org.alfresco.repo.transaction.RetryingTransactionHelper.RetryingTransactionCallback;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.util.ParameterCheck;

import com.tieto.ecm.alfresco.monitor.storage.MonitorStorage;
import com.tieto.ecm.alfresco.monitor.storage.model.JobOperation;
import com.tieto.ecm.alfresco.monitor.storage.model.MonitorModel;

public class AclHierarchyJobImpl extends BaseProcessorExtension implements AclHierarchyJob {

	private RetryingTransactionHelper transactionHelper;
	private MonitorStorage monitorStorage;

	
	public void setTransactionHelper(RetryingTransactionHelper transactionHelper) {
		this.transactionHelper = transactionHelper;
	}

	public void setMonitorStorage(MonitorStorage monitorStorage) {
		this.monitorStorage = monitorStorage;
	}

	/* (non-Javadoc)
	 * @see com.tieto.ecm.alfresco.monitor.service.job.AclHierarchyJob#createAclHierarchyJob(long)
	 */
	@Override
	public NodeRef createAclHierarchyJob(long depth) {
		ParameterCheck.mandatory("depth", depth);
	    return transactionHelper.doInTransaction(new RetryingTransactionCallback<NodeRef>() {
            @Override
            public NodeRef execute() throws Throwable {
            	final NodeRef monitorNode = monitorStorage.createMonitorJobNode(MonitorModel.TYPE_MONITOR_ACL_HIERARCHY);
            	monitorStorage.setOperation(monitorNode, JobOperation.NODES_HIERARCHY);
            	monitorStorage.addDepth(monitorNode, depth);
                return monitorNode;
            }
        }, false, true);
	}
}
