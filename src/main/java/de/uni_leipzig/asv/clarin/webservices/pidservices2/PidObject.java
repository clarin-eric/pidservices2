package de.uni_leipzig.asv.clarin.webservices.pidservices2;

import java.util.Map;

import net.sf.json.JSONArray;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

/**
 * Stores most relevant information of a PID JSON object retrieved from the GWDG
 * 
 * @author Thomas Eckart
 * 
 */
public class PidObject {
	private final String handleIdentifier;
	private Map<HandleField, String> fieldMap;

	public PidObject(String pid, JSONArray pidJsonArray) {
		this.handleIdentifier = pid;

		String jsonPath;
		for (HandleField fieldName : HandleField.values()) {
			try {
				jsonPath = "$..[?(@.type=='" + fieldName + "')].parsed_data[0]";
				fieldMap.put(fieldName, JsonPath.read(pidJsonArray, jsonPath).toString());
			} catch (PathNotFoundException pnfe) {
				fieldMap.put(fieldName, null);
			}
		}
	}

	/**
	 * Returns handle identifier
	 * 
	 * @return handle identifier
	 */
	public String getHandleIdentifier() {
		return handleIdentifier;
	}

	/**
	 * Returns stored value in EPIC handle for a specific field
	 * 
	 * @param field
	 *            name of the stored field
	 * @return value of the stored field ('parsed_data'), may be NULL
	 */
	public String getValue(HandleField fieldName) {
		return fieldMap.get(fieldName);
	}
}
