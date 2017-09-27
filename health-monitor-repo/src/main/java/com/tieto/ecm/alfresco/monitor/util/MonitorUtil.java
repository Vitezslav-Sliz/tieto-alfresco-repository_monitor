package com.tieto.ecm.alfresco.monitor.util;

import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author D.Katanik, D.Grobelny
 *
 */
public final class MonitorUtil {
	public static final String DELIMETER = "~";

	public static final Logger LOGGER = LoggerFactory.getLogger(MonitorUtil.class);

	private MonitorUtil() {
		super();
	}

	public static String getMonitoredNotValidHierarchy(Set<String> hierarchyStructureSet, int validHiearchyDeep) {
		JSONArray jsonHierarchyStructures = new JSONArray();
		
		for (Iterator<String> hiearchyStructureIterator = hierarchyStructureSet.iterator(); hiearchyStructureIterator.hasNext();) {
			String hierarchyStructure = (String) hiearchyStructureIterator.next();
			String[] splitedHierarchyStructure = hierarchyStructure.split(DELIMETER);

			if (splitedHierarchyStructure.length <= validHiearchyDeep)
				continue;

			JSONArray jsonHierarchyStructure = null;
			try {
				jsonHierarchyStructure = new JSONArray(splitedHierarchyStructure);
			} catch (JSONException e) {
				LOGGER.error(e.getMessage());
				continue;
			}

			jsonHierarchyStructures.put(jsonHierarchyStructure);
		}

		return jsonHierarchyStructures.toString();
	}

}
