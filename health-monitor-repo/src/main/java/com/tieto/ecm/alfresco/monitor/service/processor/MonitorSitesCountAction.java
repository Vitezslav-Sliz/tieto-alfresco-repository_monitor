package com.tieto.ecm.alfresco.monitor.service.processor;


import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchParameters;
import org.alfresco.service.cmr.search.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tieto.ecm.alfresco.monitor.service.output.builder.JSONSitesBuilder;
import com.tieto.ecm.alfresco.monitor.storage.model.JobStatus;

/**
 * Class implements processor which calculate number of sites
 * 
 * @author Vojtech Molin
 * @author Vitezslav Sliz (vitezslav.sliz@tieto.com)
 * @version 1.0
 */
public class MonitorSitesCountAction extends AbstractMonitorExecuterAction {

	private static final Logger LOGGER = LoggerFactory.getLogger(MonitorSitesCountAction.class);
	private static final String QUERY = "select * from st:site";
	
	private SearchService searchService;
	
	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

	@Override
	protected void executeImpl(NodeRef actionedUponNodeRef) {
		
		final SearchParameters sp = new SearchParameters();
		sp.addStore(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE);
		sp.setLanguage(SearchService.LANGUAGE_CMIS_ALFRESCO);
		sp.setQuery(QUERY);
		try {
			final ResultSet rs = searchService.query(sp);
			LOGGER.info("Actual number of sites: {}", rs.length());
			final String message = rs.length() < 5000?"Count of sites is OK":"Count of sites exceed recomended limit";
			setOutput(actionedUponNodeRef, new JSONSitesBuilder().setSitesCount(rs.length()).generateOutput());
			updateStatus(actionedUponNodeRef, JobStatus.Status.FINISHED, message);
		} catch (RuntimeException e) {
			updateStatus(actionedUponNodeRef, JobStatus.Status.ERROR, "Error to get number of sites.");
		}
	}
}