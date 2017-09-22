package com.tieto.ecm.alfresco.monitor.storage;

/**
 * 
 * @author Vitezslav Sliz (vitezslav.sliz@tieto.com)
 * @version 1.0
 */
public class AclHierarchyMonitorRequest {
    
    private long groupsDepth;

	public AclHierarchyMonitorRequest(long groupsDepth) {
		super();
		this.groupsDepth = groupsDepth;
	}

	public long getGroupsDepth() {
		return groupsDepth;
	}

}