package com.tieto.ecm.alfresco.monitor.service;

import java.util.Map;

import org.alfresco.repo.transaction.RetryingTransactionHelper;
import org.alfresco.repo.transaction.RetryingTransactionHelper.RetryingTransactionCallback;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.util.ParameterCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tieto.ecm.alfresco.monitor.action.ActionServiceHelper;
import com.tieto.ecm.alfresco.monitor.storage.MonitorStorage;
import com.tieto.ecm.alfresco.monitor.storage.model.JobOperation;
import com.tieto.ecm.alfresco.monitor.storage.model.JobStatus;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.repo.processor.BaseProcessorExtension;


/**
 * Class implement Monitor request handling
 * 
 * @author Vitezslav Sliz (vitezslav.sliz@tieto.com)
 * @version 1.0
 */
public class MonitorServiceImpl extends BaseProcessorExtension implements MonitorJobService{

	private static final Logger LOGGER = LoggerFactory.getLogger(MonitorServiceImpl.class);
	
	// Dependencies
    private Map<JobOperation,ActionServiceHelper> serviceHelpers;
    
	private MonitorStorage monitorStorage;
    private RetryingTransactionHelper transactionHelper;
    
    // Dependency setters
    
    public void setTransactionHelper(RetryingTransactionHelper transactionHelper) {
        this.transactionHelper = transactionHelper; 
    }

	public void setServiceHelpers(Map<JobOperation, ActionServiceHelper> serviceHelpers) {
		this.serviceHelpers = serviceHelpers;
	}

	public void setMonitorStorage(MonitorStorage monitorStorage) {
		this.monitorStorage = monitorStorage;
	}
	
	@Override
	public JobStatus getMonitorStatus(NodeRef monitorNodeRef) {
		ParameterCheck.mandatory("monitorNodeRef", monitorNodeRef);
		return monitorStorage.getMonitorStatus(monitorNodeRef);
	}

	@Override
	public JobOperation getMonitorOperation(NodeRef monitorNodeRef) {
		ParameterCheck.mandatory("monitorNodeRef", monitorNodeRef);
		return monitorStorage.getMonitorOperation(monitorNodeRef);
	}

	@Override
	public void runMonitorOperation(NodeRef monitorNodeRef) {
		ParameterCheck.mandatory("monitorNodeRef", monitorNodeRef);
		boolean runProcess = transactionHelper.doInTransaction(new RetryingTransactionCallback<Boolean>() {
            @Override
            public Boolean execute() throws Throwable {
        		final JobStatus status = getMonitorStatus(monitorNodeRef);
        		if (status.getStatus() == JobStatus.Status.PENDING){
        			monitorStorage.updateStatus(monitorNodeRef, new JobStatus(JobStatus.Status.PROCESSING,null));
        			return true;
        		}
        		return false;
            }
        }, false, true);
		if(runProcess){
			final JobOperation oper = getMonitorOperation(monitorNodeRef);
			final ActionServiceHelper action = serviceHelpers.get(oper); 
			if (action != null){
				action.executeAction(monitorNodeRef);
			}else{
				LOGGER.error("Not registered action for type: MonitorAction.{}",oper);
				throw new AlfrescoRuntimeException(String.format("Not registered action for type: MonitorAction.%s", oper));
			}
		}
	}
}
