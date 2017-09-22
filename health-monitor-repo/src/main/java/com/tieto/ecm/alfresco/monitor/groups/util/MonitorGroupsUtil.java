package com.tieto.ecm.alfresco.monitor.groups.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.repo.domain.node.ContentDataWithId;
import org.alfresco.service.namespace.NamespacePrefixResolver;
import org.alfresco.service.namespace.QName;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class MonitorGroupsUtil {
	public static final String MODEL = "model";
	// FIXME Readed property value will not be decrypted if is encrypted
	// FIXME Readed property value will not be decrypted if is encrypted
	public static final String NODEREF = "nodeRef";
	public static final String REQUEST_ID = "requestId";
	public static final String DIRECTORY = "directory";
	public static final String FILENAME = "fileName";
	public static final String ERROR = "error";
	public static final String ERROR_JSON = "error_json";
	public static final String ERROR_INVALID_ID = "error_invalid_id";
	public static final String RESULT = "result";
	public static final String SUCCESS = "success";
	public static final String STATUS_REGEX = ",";
	public static final String NODES = "nodes";
	public static final String DOCUMENT_ID = "documentId";
	public static final String STATUS = "status";
	public static final String MESSAGE = "message";
	public static final String WORKSPACE = "workspace://SpacesStore/%s";
	public static final String EXCEPTION = "exception";
	public static final String OBJECT_NOT_FOUND = "objectNotFound";
	public static final String TIME = "TIME";
	public static final String SERVER = "SERVER";
	public static final String DATA = "DATA";
	public static final Logger logger = LoggerFactory.getLogger(MonitorGroupsUtil.class);

	private MonitorGroupsUtil() {
		super();
	}

	

	public static Map<String, Object> errorResult(String string) {
		final Map<String, Object> resultMap = new LinkedHashMap<>();
		resultMap.put(MonitorGroupsUtil.RESULT, MonitorGroupsUtil.ERROR);
		resultMap.put(MonitorGroupsUtil.ERROR, string);
		return resultMap;
	}

	public static Map<String, Object> nodeNotExist(String nodeRefString) {
		final Map<String, Object> resultMap = new LinkedHashMap<>();
		resultMap.put(MonitorGroupsUtil.NODEREF, nodeRefString);
		resultMap.put(MonitorGroupsUtil.RESULT, MonitorGroupsUtil.ERROR);
		resultMap.put(MonitorGroupsUtil.ERROR, String.format("Node is not exist - nodeRef = %s", nodeRefString));
		return resultMap;
	}

	

	public static Map<String, Object> recordAddedResult(String triggerNode) {
		final Map<String, Object> resultMap = new LinkedHashMap<>();
		final Map<String, Object> model = new LinkedHashMap<>();
		model.put(MonitorGroupsUtil.REQUEST_ID, triggerNode);
		model.put(MonitorGroupsUtil.RESULT, MonitorGroupsUtil.SUCCESS);
		resultMap.put(MODEL, new JSONObject(model).toString());
		return resultMap;
	}

	public static Map<String, Object> errorJsonBroken() {
		final Map<String, Object> resultMap = new LinkedHashMap<>();
		final Map<String, Object> model = new LinkedHashMap<>();
		model.put(MonitorGroupsUtil.RESULT, MonitorGroupsUtil.ERROR_JSON);
		model.put(MonitorGroupsUtil.MESSAGE, "JSON object is broken");
		resultMap.put(MODEL, new JSONObject(model).toString());
		return resultMap;
	}

	public static Map<String, Object> errorInvalidId(String message2) {
		final Map<String, Object> model = new LinkedHashMap<>();
		model.put(MonitorGroupsUtil.RESULT, MonitorGroupsUtil.ERROR_INVALID_ID);
		model.put(MonitorGroupsUtil.MESSAGE, message2);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put(MODEL, new JSONObject(model).toString());
		return resultMap;
	}

	public static Map<String, Object> errorObjectNotFound(String message) {
		final Map<String, String> model = new HashMap<>();
		model.put(MonitorGroupsUtil.EXCEPTION, MonitorGroupsUtil.OBJECT_NOT_FOUND);
		model.put(MonitorGroupsUtil.ERROR, message);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put(MODEL, new JSONObject(model).toString());
		return resultMap;
	}

	public static Map<String, Object> getMetadata(Map<QName, Serializable> documentProperties,
			NamespacePrefixResolver resolver) {
		Map<String, Object> model = documentProperties.entrySet().stream().filter(p -> p.getValue() != null)
				.collect(Collectors.toMap(p -> ((QName) p.getKey()).toPrefixString(resolver),
						p -> (Object) dateToUTCString(p.getValue())));
		putSizeKeytoModel(model);
		Map<String, Object> map = new HashMap<>();
		map.put(MODEL, new JSONObject(model).toString());
		return map;
	}
	
	public static Map<String, Object> getMonitoredNotValidGroups(Set<String> set, int deep) {
		final Map<String, Object> model = new HashMap<>();
		model.put(MonitorGroupsUtil.TIME, LocalDateTime.now().toString());
		model.put(MonitorGroupsUtil.SERVER, "HOST");
		
		JSONArray data = new JSONArray();
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			String[] split = string.split("~");
			
			if (split.length <= deep)
				continue;
			
			JSONArray arr = null;
			try {
				arr = new JSONArray(split);
			} catch (JSONException e) {
				logger.error(e.getMessage());
				continue;
			}
			
			data.put(arr);
		}
		
		model.put(MonitorGroupsUtil.DATA,data.toString());
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put(MODEL, new JSONObject(model).toString());
		return resultMap;
	}

	public static void putSizeKeytoModel(Map<String, Object> model) {

		ContentDataWithId value = (ContentDataWithId) model.get("cm:content");
		if (value == null) {
			return;
		}

		model.put("size", value.getSize());
	}

	public static Object dateToUTCString(Object object) {
		if (object instanceof Date) {
			SimpleDateFormat dateFormatUFC = new SimpleDateFormat("MM/dd/yyyy KK:mm:ss");
			dateFormatUFC.setTimeZone(TimeZone.getTimeZone("UTC"));
			return dateFormatUFC.format(object);
		} else
			return object;
	}

	

	public static <T> T deepCopyJAXB(T object, Class<T> clazz) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
			JAXBElement<T> contentObject = new JAXBElement<T>(new javax.xml.namespace.QName(clazz.getSimpleName()),
					clazz, object);
			JAXBSource source = new JAXBSource(jaxbContext, contentObject);
			return jaxbContext.createUnmarshaller().unmarshal(source, clazz).getValue();
		} catch (JAXBException e) {
			logger.debug(e.getMessage(), e);
			logger.error(e.getMessage());
			throw new AlfrescoRuntimeException(e.getMessage());
		}
	}

	public static <T> T deepCopyJAXB(T object) {
		if (object == null)
			throw new AlfrescoRuntimeException("Can't guess at class");
		return deepCopyJAXB(object, (Class<T>) object.getClass());
	}

	public static boolean validValue(String value) {
		if (value == null) {
			return false;
		}
		Pattern pattern = Pattern.compile("#[A-Z]{3}[0-9]{2}");
		Matcher matcher = pattern.matcher(value);
		return matcher.find();
	}

	public static boolean isRfc5987Supported(String userAgent) {
		return Stream.of("msie", " trident/", " chrome/", " firefox/", " safari/").anyMatch(userAgent::contains);
	}

}
