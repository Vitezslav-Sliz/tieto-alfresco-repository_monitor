package com.tieto.ecm.alfresco.monitor.groups.service;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.alfresco.service.cmr.security.AuthorityService;
import org.alfresco.service.cmr.security.AuthorityType;
import org.alfresco.util.ParameterCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class MonitorGroupsService implements InitializingBean {
	private AuthorityService authorityService;
	private static Logger LOGGER = LoggerFactory.getLogger(MonitorGroupsService.class);

	public Map<String, Set<String>> executeImpl() {
		Map<String, Set<String>> map = new LinkedHashMap<>();

		findGroups(null, map, "*");

		return map;
	}

	private void findGroups(String root, Map<String, Set<String>> map, String currentKey) {

		Set<String> groups = authorityService.findAuthorities(AuthorityType.GROUP, root, true, "*", null);
		
		map.put(currentKey, groups);

		if (groups != null || !groups.isEmpty()) {
			for (Iterator iterator = groups.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				findGroups(string, map, currentKey.concat("~").concat(string));
			}
		}

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ParameterCheck.mandatory("authorityService", authorityService);
	}

	public void setAuthorityService(AuthorityService authorityService) {
		this.authorityService = authorityService;
	}

}
