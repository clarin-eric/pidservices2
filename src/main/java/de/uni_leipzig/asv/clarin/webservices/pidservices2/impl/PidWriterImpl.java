package de.uni_leipzig.asv.clarin.webservices.pidservices2.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import de.uni_leipzig.asv.clarin.webservices.pidservices2.Configuration;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.HandleField;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.interfaces.PidWriter;

/**
 * Registering new handles at handle server or modifying existing PID entries
 * 
 * @author Thomas Eckart
 * 
 */
public class PidWriterImpl implements PidWriter {
	private final static Logger LOG = Logger.getLogger(PidWriterImpl.class);

	public String registerNewPID(final Configuration configuration, Map<HandleField, String> fieldMap)
			throws HttpException {
		LOG.info("Try to create handle at " + configuration.getServiceBaseURL() + " with values: " + fieldMap);

		try {
			final Client client = Client.create();
			client.addFilter(new HTTPBasicAuthFilter(configuration.getUser(), configuration.getPassword()));
			final WebResource webResource = client.resource(configuration.getServiceBaseURL()
					+ configuration.getHandlePrefix());

			JSONArray jsonArray = createJSONArray(fieldMap);

			final ClientResponse response = webResource.accept("application/json").type("application/json")
					.post(ClientResponse.class, jsonArray.toString());
			if (response.getStatus() != 201) {
				throw new HttpException("" + response.getStatus());
			}

			// TODO extract new Handle
			return response.getEntity(String.class);
		} catch (final NullPointerException npe) {
			return null;
		}
	}

	public void modifyPid(final Configuration configuration, final String pid, Map<HandleField, String> fieldMap) {
		LOG.info("Try to modify handle \"" + pid + "\" at " + configuration.getServiceBaseURL() + " with new values: "
				+ fieldMap);

		final Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter(configuration.getUser(), configuration.getPassword()));
		final WebResource webResource = client.resource(configuration.getServiceBaseURL() + pid);

		JSONArray jsonArray = createJSONArray(fieldMap);
		System.out.println(jsonArray.toString());
		webResource.accept("application/json").type("application/json").put(ClientResponse.class, jsonArray.toString());
	}

	/**
	 * Generates JSON array that is understood by the EPIC handle service
	 * 
	 * @param fieldMap
	 *            mapping handle field -> value
	 * @return JSON array
	 */
	private JSONArray createJSONArray(Map<HandleField, String> fieldMap) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;

		Iterator<HandleField> fieldIter = fieldMap.keySet().iterator();
		while (fieldIter.hasNext()) {
			jsonObject = new JSONObject();
			HandleField handleFieldTyp = fieldIter.next();
			jsonObject.put("type", handleFieldTyp);
			jsonObject.put("parsed_data", fieldMap.get(handleFieldTyp));
			jsonArray.add(jsonObject);
		}

		return jsonArray;
	}

	public static void main(String[] args) throws HttpException {
		PidWriterImpl writer = new PidWriterImpl();
		Map<HandleField, String> handleFieldMap = new HashMap<HandleField, String>();
		handleFieldMap.put(HandleField.URL, "http://asv.informatik.uni-leipzig.de");
		handleFieldMap.put(HandleField.TITLE, "Test Title");
		handleFieldMap.put(HandleField.AUTHORS, "Thomas Eckart");
		// String newHandle = writer.registerNewPID(Configuration.getInstance(), handleFieldMap);
		// System.out.println(newHandle);

		String pid = "11022/0000-0000-1F9F-C";
		writer.modifyPid(Configuration.getInstance(), pid, handleFieldMap);
	}
}
