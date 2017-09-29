package com.tieto.ecm.alfresco.monitor.service.output.builder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.tieto.ecm.alfresco.monitor.service.output.MonitorOutput;

public class JSONSitesBuilder implements MonitorOutput {
	
	private static final Logger logger = Logger.getLogger(JSONSitesBuilder.class);
	
	private long sitesCount;
	
	public JSONSitesBuilder setSitesCount(long sitesCount) {
		this.sitesCount = sitesCount;
		return this;
	}

	@Override
	public String generateOutput() {
		final JSONObject json = new JSONObject();
		try {
			try {
				json.put("server", InetAddress.getLocalHost().getHostName());
			} catch (UnknownHostException e) {
				logger.error("Problem to get server address",e);
				json.put("server", "UNKNOWN");
			}
			json.put("time", LocalDateTime.now().toString());
			json.put("sitesCount", sitesCount);

		} catch (JSONException e) {
			logger.error("Error during JSON creation: ", e);
		}
		return json.toString();
	}

}
