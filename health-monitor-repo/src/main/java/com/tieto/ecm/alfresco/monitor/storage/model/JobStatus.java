package com.tieto.ecm.alfresco.monitor.storage.model;

import java.io.Serializable;

/**
 * 
 * @author Vitezslav Sliz (vitezslav.sliz@tieto.com)
 * @version 1.0
 */
public class JobStatus implements Serializable {

	private static final long serialVersionUID = 7548721117584029871L;

	public enum Status {
	    /**
	     * Request queued
	     */
	    PENDING,
	    /**
	     * Request processing
	     */
	    PROCESSING,

	    /**
	     * Request finished processing
	     */
	    FINISHED,
	    /**
	     * Request not processed correctly
	     */
	    ERROR,
	}

	/**
	 * Actual Status of request 
	 */
	private final Status status;
	
	private final String message;
	
	public JobStatus(final Status status,final String message) {
		this.status = status;
		this.message = message;
	}
	
	 /**
     * @return true if status is FINISHED or ERROR false otherwise.
     */
	public boolean isComplete()
    {
        return status == Status.FINISHED || status == Status.ERROR;
    }

	/**
	 * @return the current status
	 */
    public Status getStatus()
    {
        return status;
    }

	/**
	 * @return return info message
	 */
	public String getMessage() {
		return message;
	}
    
}
