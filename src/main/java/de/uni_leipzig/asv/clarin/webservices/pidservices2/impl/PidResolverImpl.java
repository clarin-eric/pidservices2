package de.uni_leipzig.asv.clarin.webservices.pidservices2.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import de.uni_leipzig.asv.clarin.webservices.pidservices2.Configuration;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.HandleField;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.PidObject;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.interfaces.PidResolver;

/**
 * Requests information about handle from handle server
 * 
 * @author Thomas Eckart
 * 
 */
public class PidResolverImpl implements PidResolver {
	private final static Logger LOG = Logger.getLogger(PidResolverImpl.class);

	public JSONArray resolvePidAsJSON(final Configuration configuration, final String pid) throws IOException {
		LOG.info("Searching for \"" + pid + "\" at " + configuration.getServiceBaseURL());

		final Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter(configuration.getUser(), configuration.getPassword()));
		final WebResource webResource = client.resource(configuration.getServiceBaseURL() + pid);

		// query
		final ClientResponse clientResponse = webResource.accept("application/json;charset=UTF-8").get(
				ClientResponse.class);
		if (clientResponse.getStatus() != 200) {
			throw new IOException("Received a different response than expected (200): " + clientResponse.getStatus()
					+ " (URL: '" + webResource.toString() + "')");
		}

		return JSONArray.fromObject(clientResponse.getEntity(String.class));
	}

	public PidObject resolvePidAsPOJO(final Configuration configuration, final String pid) throws IOException {
		return new PidObject(pid, resolvePidAsJSON(configuration, pid));
	}

	public Map<String, JSONArray> searchPidAsJSON(final Configuration configuration,
			Map<HandleField, String> fieldMap) throws IOException {
		LOG.info("Searching at " + configuration.getServiceBaseURL() + " with: " + fieldMap);
		Map<String, JSONArray> jsonArrayMap = new HashMap<String, JSONArray>();

		final Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter(configuration.getUser(), configuration.getPassword()));
		final WebResource webResource = client.resource(configuration.getServiceBaseURL()
				+ configuration.getHandlePrefix());

		// add URL parameters
		final MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		Iterator<HandleField> fieldTypeIterator = fieldMap.keySet().iterator();
		while (fieldTypeIterator.hasNext()) {
			HandleField tmpFieldType = fieldTypeIterator.next();
			queryParams.add(tmpFieldType.toString(), fieldMap.get(tmpFieldType));
		}

		// query
		final ClientResponse response = webResource.queryParams(queryParams).accept("application/json;charset=UTF-8")
				.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new IOException("Received a different response than expected (200): " + response.getStatus()
					+ " (URL: '" + webResource.toString() + "')");
		}

		// parse response and get all handle fields
		JSONArray handleIdJSONArray = JSONArray.fromObject(response.getEntity(String.class));
		for (int i = 0; i < handleIdJSONArray.size(); i++) {
			String handle = Configuration.getInstance().getHandlePrefix() + "/" + handleIdJSONArray.getString(i);
			LOG.debug("Found handle " + i + "\t" + handle);
			jsonArrayMap.put(handle, resolvePidAsJSON(configuration, handle));
		}

		return jsonArrayMap;
	}

	public Map<String, PidObject> searchPidAsPOJO(final Configuration configuration,
			Map<HandleField, String> fieldMap) throws IOException {
		Map<String, JSONArray> jsonArrayMap = searchPidAsJSON(configuration, fieldMap);
		Map<String, PidObject> pidObjectsMap = new HashMap<String, PidObject>();
		Iterator<String> handleIterator = jsonArrayMap.keySet().iterator();
		while (handleIterator.hasNext()) {
			String handle = handleIterator.next();
			pidObjectsMap.put(handle, new PidObject(handle, jsonArrayMap.get(handle)));
		}

		return pidObjectsMap;
	}

	public static void main(String[] args) throws IOException {
		PidResolverImpl resolver = new PidResolverImpl();
		String pid = "11022/00-GWDG-00000000005D-9";
		Map<HandleField, String> handleFieldMap = new HashMap<HandleField, String>();
		handleFieldMap.put(HandleField.URL, "http://asv.informatik.uni-leipzig.de");
		// handleFieldMap.put(HandleFieldType.TITLE, "Test Title");
		// handleFieldMap.put(HandleFieldType.AUTHORS, "Thomas Eckart");
		PidObject pidObject = resolver.resolvePidAsPOJO(Configuration.getInstance(), pid);
		System.out.println(pidObject.getValue(HandleField.URL));
		System.out.println(resolver.searchPidAsJSON(Configuration.getInstance(), handleFieldMap));

	}
}
