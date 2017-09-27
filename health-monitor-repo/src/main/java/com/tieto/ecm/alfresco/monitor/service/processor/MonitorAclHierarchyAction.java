package com.tieto.ecm.alfresco.monitor.service.processor;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.security.AuthorityService;
import org.alfresco.service.cmr.security.AuthorityType;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tieto.ecm.alfresco.monitor.storage.MonitorStorage;
import com.tieto.ecm.alfresco.monitor.storage.model.JobStatus;
import com.tieto.ecm.alfresco.monitor.storage.model.JobStatus.Status;
import com.tieto.ecm.alfresco.monitor.util.MonitorUtil;

/**
 * 
 * @author D.Katanik, D.Grobelny
 *
 */
public class MonitorAclHierarchyAction extends AbstractMonitorExecuterAction {
	private static final String STARTING_FOLDER_STRUCTURE_KEY = "*";
	private static final String DELIMETER = "~";
	private static final int DEEP = 5;
	private AuthorityService authorityService;
	private MonitorStorage monitorStorage;
	private static final Logger LOGGER = LoggerFactory.getLogger(MonitorAclHierarchyAction.class);

	@Override
	protected void executeImpl(NodeRef actionedUponNodeRef) {
		Map<String, Set<String>> map = new LinkedHashMap<>();
		
		findGroups(null, map, STARTING_FOLDER_STRUCTURE_KEY);
		
		String monitoredNotValidHierarchy = MonitorUtil.getMonitoredNotValidHierarchy(map.keySet(), DEEP);

		try {
			monitorStorage.setMonitorData(actionedUponNodeRef, IOUtils.toInputStream(monitoredNotValidHierarchy, "UTF-8"));
			monitorStorage.updateStatus(actionedUponNodeRef, new JobStatus(Status.FINISHED, "FINISHED"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return;
		}
	}
	
	private void findGroups(String rootFolder, Map<String, Set<String>> foldersStructuresResult, String currentFolderStructureKey) {
		Set<String> currentImmediateChildGroups = authorityService.findAuthorities(AuthorityType.GROUP, rootFolder, true, STARTING_FOLDER_STRUCTURE_KEY, null);
		
		foldersStructuresResult.put(currentFolderStructureKey, currentImmediateChildGroups);

		if (currentImmediateChildGroups != null && !currentImmediateChildGroups.isEmpty()) {
			for (Iterator<String> immediateChildGroupIterator = currentImmediateChildGroups.iterator(); immediateChildGroupIterator.hasNext();) {
				String immediateChildGroup = (String) immediateChildGroupIterator.next();
				
				findGroups(immediateChildGroup, foldersStructuresResult, currentFolderStructureKey.concat(DELIMETER).concat(immediateChildGroup));
			}
		}
	}

	public void setAuthorityService(AuthorityService authorityService) {
		this.authorityService = authorityService;
	}

	public void setMonitorStorage(MonitorStorage monitorStorage) {
		this.monitorStorage = monitorStorage;
	}

}
