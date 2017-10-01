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
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tieto.ecm.alfresco.monitor.storage.MonitorStorage;
import com.tieto.ecm.alfresco.monitor.storage.model.JobStatus;
import com.tieto.ecm.alfresco.monitor.storage.model.JobStatus.Status;

/**
 * 
 * @author D.Katanik
 * @author D.Grobelny
 *
 */
public class MonitorAclHierarchyAction extends AbstractMonitorExecuterAction {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MonitorAclHierarchyAction.class);
	
	private static final String ROOT_KEY = "*";
	private static final String DELIMETER = "~";
	
	private AuthorityService authorityService;
	private MonitorStorage monitorStorage;

	@Override
	protected void executeImpl(NodeRef actionedUponNodeRef) {
		Map<String, Set<String>> map = new LinkedHashMap<>();
		final long depth = monitorStorage.getAclRequest(actionedUponNodeRef).getGroupsDepth();
		
		findGroups(null, map, ROOT_KEY);

		// Send only keys from founded groups
		String monitoredNotValidHierarchy = getMonitoredNotValidHierarchy(map.keySet(), depth);

		try {
			monitorStorage.setMonitorData(actionedUponNodeRef, IOUtils.toInputStream(monitoredNotValidHierarchy, "UTF-8"));
			monitorStorage.updateStatus(actionedUponNodeRef, new JobStatus(Status.FINISHED, "FINISHED"));
		} catch (IOException e) {
			monitorStorage.updateStatus(actionedUponNodeRef, new JobStatus(Status.ERROR, e.getMessage()));
			LOGGER.error(e.getMessage());
			return;
		}
	}
	
	/**
	 * Recursively finds groups hierarchy. 
	 * 
	 * @see {@link AuthorityService#findAuthorities(AuthorityType, String, boolean, String, String)}
	 * 
	 * @param rootGroup based on parentAuthority if non-null, will look only for authorities who are a child of the named parent
	 * @param groupsHierarchyResult result of recursive function
	 * @param currentGroupHierarchyKey Actual group hierarchy structure
	 */
	private void findGroups(String rootGroup, Map<String, Set<String>> groupsHierarchyResult, String currentGroupHierarchyKey) {
		Set<String> currentImmediateChildGroups = authorityService.findAuthorities(AuthorityType.GROUP, rootGroup, true, ROOT_KEY, null);
		
		// Put founded immediate groups to map with current group hierarchy key
		groupsHierarchyResult.put(currentGroupHierarchyKey, currentImmediateChildGroups);

		// If there is some child groups call find groups for each child group again, otherwise do nothing.
		if (currentImmediateChildGroups != null && !currentImmediateChildGroups.isEmpty()) {
			for (Iterator<String> immediateChildGroupIterator = currentImmediateChildGroups.iterator(); immediateChildGroupIterator.hasNext();) {
				String immediateChildGroup = (String) immediateChildGroupIterator.next();
				
				findGroups(immediateChildGroup, groupsHierarchyResult, currentGroupHierarchyKey.concat(DELIMETER).concat(immediateChildGroup));
			}
		}
	}
	
	/**
	 * Splits hierarchy structure set and converts it to JSON format
	 * 
	 * e.g. : [["*","g1","g2",..],["*","gA","gB",...]]
	 *  
	 * @param hierarchyStructureSet set of structure in specific format "*~g1~g2~..."
	 * @param depth of invalid hierarchy structure
	 * @return groups hierarchy structure in JSON
	 */
	private String getMonitoredNotValidHierarchy(Set<String> hierarchyStructureSet, long depth) {
		JSONArray jsonHierarchyStructures = new JSONArray();
		
		for (Iterator<String> hiearchyStructureIterator = hierarchyStructureSet.iterator(); hiearchyStructureIterator.hasNext();) {
			String hierarchyStructure = (String) hiearchyStructureIterator.next();
			String[] splittedHierarchyStructure = hierarchyStructure.split(DELIMETER);

			// If splitted structure of group is less than control depth continue 
			if (splittedHierarchyStructure.length <= depth)
				continue;

			JSONArray jsonHierarchyStructure = null;
			try {
				jsonHierarchyStructure = new JSONArray(splittedHierarchyStructure);
			} catch (JSONException e) {
				LOGGER.error(e.getMessage());
				continue;
			}

			jsonHierarchyStructures.put(jsonHierarchyStructure);
		}

		return jsonHierarchyStructures.toString();
	}

	public void setAuthorityService(AuthorityService authorityService) {
		this.authorityService = authorityService;
	}

	public void setMonitorStorage(MonitorStorage monitorStorage) {
		this.monitorStorage = monitorStorage;
	}

}
