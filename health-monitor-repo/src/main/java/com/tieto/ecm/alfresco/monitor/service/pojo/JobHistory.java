package com.tieto.ecm.alfresco.monitor.service.pojo;

import org.alfresco.service.cmr.repository.NodeRef;

public class JobHistory {
	final String operation;
	final NodeRef nodeRef;
	final String status;
	
	public JobHistory(final String operation,final NodeRef nodeRef, String status) {
		super();
		this.operation = operation;
		this.nodeRef = nodeRef;
		this.status = status;
	}

	public NodeRef getNodeRef() {
		return nodeRef;
	}

	public String getStatus() {
		return status;
	}

	public String getType() {
		return operation;
	}
}
