package com.tieto.ecm.alfresco.monitor.storage;

import org.alfresco.service.cmr.repository.NodeRef;

/**
 * 
 * @author Vitezslav Sliz (vitezslav.sliz@tieto.com)
 * @version 1.0
 */
public final class NodesHierarchyMonitorRequest {
	
	private final NodeRef rootSourcePathNode;
	
	public NodesHierarchyMonitorRequest(final NodeRef rootSourcePathNode) {
		super();
		this.rootSourcePathNode = rootSourcePathNode;
	}
	
	public NodeRef getRootSourcePathNode() {
		return rootSourcePathNode;
	}
}
