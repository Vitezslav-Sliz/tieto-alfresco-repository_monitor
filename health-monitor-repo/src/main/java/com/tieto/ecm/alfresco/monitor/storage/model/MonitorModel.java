package com.tieto.ecm.alfresco.monitor.storage.model;

import org.alfresco.service.namespace.QName;

/**
 * Interface represent custom model needed for monitor
 * 
 * @see monitor-model.xml 
 * @author Vitezslav Sliz (vitezslav.sliz@tieto.com)
 * @version 1.0
 */
public interface MonitorModel {
	
	public static final String MODEL_DELIMITER = ":";
    
	/**
     * Monitor Model URI: <i>{http://www.tieto.com/model/MonitorContent/1.0}</i>
     */
	public static final String TIETO_MONITOR_MODEL_1_0_URI = "http://www.tieto.com/model/MonitorContent/1.0";
    
	public static final QName TYPE_MONITOR = QName.createQName(TIETO_MONITOR_MODEL_1_0_URI, "monitor");
	
	public static final QName TYPE_MONITOR_NODES_HIERARCHY = QName.createQName(TIETO_MONITOR_MODEL_1_0_URI, "nodesHierarchy");

	public static final QName TYPE_MONITOR_ACL_HIERARCHY = QName.createQName(TIETO_MONITOR_MODEL_1_0_URI, "aclHierarchy");
	
	public static final QName TYPE_MONITOR_SITES_COUNT = QName.createQName(TIETO_MONITOR_MODEL_1_0_URI, "sitesCount");

	public static final QName PROP_OPERATION = QName.createQName(TIETO_MONITOR_MODEL_1_0_URI, "operation");
	
	public static final QName PROP_STATUS = QName.createQName(TIETO_MONITOR_MODEL_1_0_URI, "status");
	
	public static final QName PROP_MESSAGE = QName.createQName(TIETO_MONITOR_MODEL_1_0_URI, "message");

	public static final QName ASSOC_SOURCE_NODE_PATH = QName.createQName(TIETO_MONITOR_MODEL_1_0_URI, "sourceNodeRefPath");

	public static final QName PROP_ACL_DEPTH = QName.createQName(TIETO_MONITOR_MODEL_1_0_URI, "aclDepth");
	
	public static final QName PROP_HIERARCHY_DEPTH = QName.createQName(TIETO_MONITOR_MODEL_1_0_URI, "hierarchyDepth");
	
	public static final QName PROP_NUMBER_OF_CHILDREN = QName.createQName(TIETO_MONITOR_MODEL_1_0_URI, "numberOfChildren");
}
