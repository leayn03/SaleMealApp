package com.meal.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author xiamingxing
 * 
 */
public class JSONFactoryUtil {

	/**
	 * @param jsonData
	 * @param targetCls
	 * @return
	 */
	public static Object parserToJavaBeanObject(Object jsonData,
			Class<?> targetCls) {

		String jsonString = null;

		Object javaBeanObject = null;

		if (null != jsonData && null != targetCls) {

			if (jsonData instanceof JSONObject || jsonData instanceof JSONArray) {

				jsonString = jsonData.toString();

			} else if (jsonData instanceof String) {

				jsonString = (String) jsonData;

			} else {

				return null;

			}

			ObjectMapper objectMapper = new ObjectMapper();

			try {

				javaBeanObject = objectMapper.readValue(jsonString, targetCls);

			} catch (Exception e) {

				e.printStackTrace();

			}

		}

		return javaBeanObject;

	}

	/**
	 * @param Object
	 * @return
	 */
	public static JSONArray parserToJsonArray(Object javaBeanList) {

		JSONArray jsonArray = null;

		String jsonString = null;

		if (null != javaBeanList) {

			if (!(javaBeanList instanceof String)) {

				jsonString = parserToJsonString(javaBeanList);

			}

			try {

				jsonArray = new JSONArray(jsonString);

			} catch (Exception e) {

				e.printStackTrace();

			}

		}

		return jsonArray;

	}

	/**
	 * @param Object
	 * @return
	 */
	public static JSONObject parserToJsonObject(Object javaBeanObject) {

		JSONObject jsonObject = null;

		String jsonString = null;

		if (null != javaBeanObject) {

			if (!(javaBeanObject instanceof String)) {

				jsonString = parserToJsonString(javaBeanObject);

			}

			try {

				jsonObject = new JSONObject(jsonString);

			} catch (Exception e) {

				e.printStackTrace();

			}

		}

		return jsonObject;

	}

	/**
	 * @param javaBeanObject
	 * @return
	 */
	public static String parserToJsonString(Object javaBeanObject) {

		String jsonString = null;

		if (null != javaBeanObject) {

			ObjectMapper objectMapper = new ObjectMapper();

			try {

				jsonString = objectMapper.writeValueAsString(javaBeanObject);

			} catch (Exception e) {

				e.printStackTrace();

			}

		}

		return jsonString;

	}

}
