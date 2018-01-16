package com.tieto.ecm.alfresco.monitor.service.pojo;

import java.util.Date;

import org.alfresco.service.cmr.repository.NodeRef;

public class JobHistory {
	final String operation;
	final NodeRef nodeRef;
	final String status;
	final Date date;
	
	public JobHistory(final String operation,final NodeRef nodeRef, String status, final Date date) {
		super();
		this.operation = operation;
		this.nodeRef = nodeRef;
		this.status = status;
		this.date = date;
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

	public Date getDate() {
		return date;
	}
}
