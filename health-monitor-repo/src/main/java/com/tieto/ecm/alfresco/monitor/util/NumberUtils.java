package com.tieto.ecm.alfresco.monitor.util;

public class NumberUtils {

	public static Integer acquireIntParameter(final String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
