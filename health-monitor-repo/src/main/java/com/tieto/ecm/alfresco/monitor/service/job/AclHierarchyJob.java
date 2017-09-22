package com.tieto.ecm.alfresco.monitor.service.job;

import org.alfresco.service.cmr.repository.NodeRef;

public interface AclHierarchyJob {

	public NodeRef createAclHierarchyJob(long depth);
}