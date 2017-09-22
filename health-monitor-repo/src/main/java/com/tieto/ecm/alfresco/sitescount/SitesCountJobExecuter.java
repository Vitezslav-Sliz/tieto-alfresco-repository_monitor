package com.tieto.ecm.alfresco.sitescount;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.apache.log4j.Logger;
import org.json.JSONObject;

public class SitesCountJobExecuter {
	private static final Logger logger = Logger.getLogger(SitesCountJobExecuter.class);

	/**
	 * Public API access
	 */
	private ServiceRegistry serviceRegistry;

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	private ResultSet resultSet;

	/**
	 * Executer implementation
	 */
	public void execute() {

		/**
		 * CMIS query search, measure it's duration and create JSON output
		 */
		logger.info("Starting Sitescount job...");

		try {
			// Get Search service
			SearchService searchService = serviceRegistry.getSearchService();

			// Choose storeRef
			StoreRef storeRef = new StoreRef(StoreRef.PROTOCOL_WORKSPACE, "SpacesStore");

			// Start time measuring
			long startTime = System.currentTimeMillis();

			// Do CMIS search
			resultSet = searchService.query(storeRef, SearchService.LANGUAGE_CMIS_ALFRESCO, "select * from st:site");

			// End time measuring
			long estimatedTime = System.currentTimeMillis() - startTime;
			logger.info(resultSet.length() + " sites has been found via CMIS in " + estimatedTime + " ms");

			// Create JSON output
			createJson(resultSet.length());

		} catch (AlfrescoRuntimeException e) {
			logger.error("Error during job: ", e);

		} finally {
			// Close result set.
			resultSet.close();
		}
	}

	/**
	 * Create JSON output
	 */
	public void createJson(int sitesCount) {
		JSONObject json = new JSONObject();

		try {
			json.put("sitesCount", sitesCount);

		} catch (Exception e) {
			logger.error("Error during JSON creation: ", e);
		}
	}
}