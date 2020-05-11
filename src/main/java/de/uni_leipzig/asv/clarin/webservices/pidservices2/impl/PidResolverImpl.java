package de.uni_leipzig.asv.clarin.webservices.pidservices2.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uni_leipzig.asv.clarin.webservices.pidservices2.Configuration;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.HandleField;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.PidObject;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.interfaces.PidResolver;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Requests information about handle from handle server
 * 
 * @author Thomas Eckart
 * @author Twan Goosen
 */
public class PidResolverImpl extends AbstractPidClient implements PidResolver {
	private final static Logger LOG = LoggerFactory.getLogger(PidResolverImpl.class);
        
	@Override
	public JSONArray resolvePidAsJSON(final Configuration configuration, final String pid) throws IOException {
		LOG.debug("Searching for \"" + pid + "\" at " + configuration.getServiceBaseURL());
		System.setProperty("jsse.enableSNIExtension", "false");

                
                final WebTarget webTarget = getWebtarget(configuration, pid);
                final Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
    
		if (response.getStatus() != 200) {
			throw new IOException("Received a different response than expected (200): " + response.getStatus()
					+ " (URL: '" + webTarget.toString() + "')");
		}

		return JSONArray.fromObject(response.readEntity(String.class));
	}

	@Override
	public PidObject resolvePidAsPOJO(final Configuration configuration, final String pid) throws IOException {
		return new PidObject(pid, resolvePidAsJSON(configuration, pid));
	}

	@Override
	public Map<String, JSONArray> searchPidAsJSON(final Configuration configuration, Map<HandleField, String> fieldMap)
			throws IOException {
		Map<String, JSONArray> jsonArrayMap = new HashMap<String, JSONArray>();

		for (String handle : searchPidAsList(configuration, fieldMap)) {
			jsonArrayMap.put(handle, resolvePidAsJSON(configuration, handle));
		}

		return jsonArrayMap;
	}

	@Override
	public Map<String, PidObject> searchPidAsPOJO(final Configuration configuration, Map<HandleField, String> fieldMap)
			throws IOException {
		Map<String, JSONArray> jsonArrayMap = searchPidAsJSON(configuration, fieldMap);
		Map<String, PidObject> pidObjectsMap = new HashMap<String, PidObject>();
		Iterator<String> handleIterator = jsonArrayMap.keySet().iterator();
		while (handleIterator.hasNext()) {
			String handle = handleIterator.next();
			pidObjectsMap.put(handle, new PidObject(handle, jsonArrayMap.get(handle)));
		}

		return pidObjectsMap;
	}

	@Override
	public List<String> searchPidAsList(final Configuration configuration, Map<HandleField, String> fieldMap)
			throws IOException {
		LOG.debug("Searching at " + configuration.getServiceBaseURL() + " with: " + fieldMap);
		List<String> handleList = new ArrayList<String>();
		System.setProperty("jsse.enableSNIExtension", "false");
                
                WebTarget webTarget = getWebtarget(configuration, configuration.getHandlePrefix()+"/");                               
                
		// add URL parameters
		Iterator<HandleField> fieldTypeIterator = fieldMap.keySet().iterator();
		while (fieldTypeIterator.hasNext()) {
			HandleField tmpFieldType = fieldTypeIterator.next();
			webTarget = webTarget.queryParam(tmpFieldType.toString(), fieldMap.get(tmpFieldType));
		}

                Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
                Response response = invocationBuilder.get();
		// query
		if (response.getStatus() != 200) {
			throw new IOException("Received a different response than expected (200): " + response.getStatus()
					+ " (URL: '" + webTarget.toString() + "')");
		}

		// parse response and get all handle fields
		JSONArray handleIdJSONArray = JSONArray.fromObject(response.readEntity(String.class));
		for (int i = 0; i < handleIdJSONArray.size(); i++) {
			String handle = configuration.getHandlePrefix() + "/" + handleIdJSONArray.getString(i);
			handleList.add(handle);
			LOG.debug("Found handle " + i + "\t" + handle);
		}

		return handleList;
	}
}