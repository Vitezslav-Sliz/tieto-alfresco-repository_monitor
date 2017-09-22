package com.tieto.ecm.alfresco.monitor.action;

import org.alfresco.service.cmr.repository.NodeRef;

/**
 * 
 * @author Vitezslav Sliz (vitezslav.sliz@tieto.com)
 * @version 1.0
 */
public interface ActionServiceHelper {
    
	/**
	 * 
     * @param node Bulk nodeRef
     */
    void executeAction(NodeRef node);
}