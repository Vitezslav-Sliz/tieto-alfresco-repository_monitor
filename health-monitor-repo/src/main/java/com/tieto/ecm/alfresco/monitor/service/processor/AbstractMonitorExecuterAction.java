package com.tieto.ecm.alfresco.monitor.service.processor;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.repo.transaction.RetryingTransactionHelper;
import org.alfresco.repo.transaction.RetryingTransactionHelper.RetryingTransactionCallback;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tieto.ecm.alfresco.monitor.storage.MonitorStorage;
import com.tieto.ecm.alfresco.monitor.storage.model.JobStatus;

/**
 * 
 * @author Vitezslav Sliz (vitezslav.sliz@tieto.com)
 * @version 1.0
 */
public abstract class AbstractMonitorExecuterAction extends ActionExecuterAbstractBase {

	private static final Logger logger = LoggerFactory.getLogger(AbstractMonitorExecuterAction.class);

	protected MonitorStorage monitorStorage;

	protected RetryingTransactionHelper transactionHelper;

	public void setTransactionHelper(RetryingTransactionHelper transactionHelper) {
		this.transactionHelper = transactionHelper;
	}
	
	public void setMonitorStorage(MonitorStorage monitorStorage) {
		this.monitorStorage = monitorStorage;
	}

	@Override
	protected final void executeImpl(Action action, NodeRef actionedUponNodeRef) {
		logger.debug("Initiate action:'{}' for NodeRef:{}", action.getActionDefinitionName(),actionedUponNodeRef.toString());
		executeImpl(actionedUponNodeRef);
		logger.debug("Action:'{}' for NodeRef:{} ended", action.getActionDefinitionName(),actionedUponNodeRef.toString());
	}

	@Override
	protected void addParameterDefinitions(List<ParameterDefinition> paramList) {
		// Not used method
	}

	protected void updateStatus(final NodeRef actionedUponNodeRef, JobStatus.Status status, String message) {
		logger.info("Update monitor status ref:{} with status:{}", actionedUponNodeRef,status);
		transactionHelper.doInTransaction(new RetryingTransactionCallback<Void>() {
			@Override
			public Void execute() throws Throwable {
				final JobStatus newStatus = new JobStatus(status, message);
				monitorStorage.updateStatus(actionedUponNodeRef, newStatus);
				return null;
			}
		}, false, true);
	}
	
	protected void setOutput(final NodeRef actionedUponNodeRef, String output) {
		transactionHelper.doInTransaction(new RetryingTransactionCallback<Void>() {
			@Override
			public Void execute() throws Throwable {
				monitorStorage.setMonitorData(actionedUponNodeRef, IOUtils.toInputStream(output, StandardCharsets.UTF_8));
				return null;
			}
		}, false, true);
	}

	/**
	 * Execute bulk operation, with handling of initialization
	 *  
	 * @param actionedUponNodeRef bulk node on which is processed execution
	 */
	abstract protected void executeImpl(NodeRef actionedUponNodeRef);
}