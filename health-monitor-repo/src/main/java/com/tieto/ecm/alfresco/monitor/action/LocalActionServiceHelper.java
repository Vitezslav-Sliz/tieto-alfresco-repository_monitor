package com.tieto.ecm.alfresco.monitor.action;

import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ActionService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.util.ParameterCheck;
import org.springframework.beans.factory.InitializingBean;

/**
 * Implement of ActionServiceHelper which schedules the monitor request process to run in the same alfresco node as the caller.
 * 
 * @author Vitezslav Sliz (vitezslav.sliz@tieto.com)
 * @version 1.0
 */
public class LocalActionServiceHelper implements InitializingBean, ActionServiceHelper {

	private ActionService localActionService;
	private String actionName;

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public void setLocalActionService(ActionService localActionService) {
		this.localActionService = localActionService;
	}

	@Override
	public void executeAction(NodeRef downloadNode) {
		Action action = localActionService.createAction(actionName);
		action.setExecuteAsynchronously(true);

		localActionService.executeAction(action, downloadNode);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ParameterCheck.mandatory("localActionServer", localActionService);
	}
}
