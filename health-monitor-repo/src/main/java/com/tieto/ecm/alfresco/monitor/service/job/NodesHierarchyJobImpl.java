package com.tieto.ecm.alfresco.monitor.service.job;

import org.alfresco.repo.processor.BaseProcessorExtension;
import org.alfresco.repo.transaction.RetryingTransactionHelper;
import org.alfresco.repo.transaction.RetryingTransactionHelper.RetryingTransactionCallback;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.util.ParameterCheck;

import com.tieto.ecm.alfresco.monitor.storage.MonitorStorage;
import com.tieto.ecm.alfresco.monitor.storage.model.MonitorModel;

public class NodesHierarchyJobImpl extends BaseProcessorExtension implements NodesHierarchyJob {

	private RetryingTransactionHelper transactionHelper;
	private MonitorStorage monitorStorage;

	
	public void setTransactionHelper(RetryingTransactionHelper transactionHelper) {
		this.transactionHelper = transactionHelper;
	}


	public void setMonitorStorage(MonitorStorage monitorStorage) {
		this.monitorStorage = monitorStorage;
	}

	/* (non-Javadoc)
	 * @see com.tieto.ecm.alfresco.monitor.service.job.NodesHierarchyJob#createNodesHuerarchyJob(org.alfresco.service.cmr.repository.NodeRef)
	 */
	@Override
	public NodeRef createNodesHierarchyJob(final NodeRef sourcePathNodeRef) {
			ParameterCheck.mandatory("sourcePathNodeRef", sourcePathNodeRef);
			return transactionHelper.doInTransaction(new RetryingTransactionCallback<NodeRef>() {
	            @Override
	            public NodeRef execute() throws Throwable {
	                //Create a download node
	                final NodeRef monitorNode = monitorStorage.createMonitorJobNode(MonitorModel.TYPE_MONITOR_NODES_HIERARCHY);
	                //Add requested nodes
	                if (sourcePathNodeRef != null){
	                	monitorStorage.addSourcePathNode(monitorNode, sourcePathNodeRef);
	                }
	                return monitorNode;
	            }
	        }, false, true);
		}
}
