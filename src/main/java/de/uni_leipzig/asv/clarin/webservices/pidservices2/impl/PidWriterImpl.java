package de.uni_leipzig.asv.clarin.webservices.pidservices2.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.Configuration;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.HandleField;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.PidApiException;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.interfaces.PidWriter;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Registering new handles at handle server or modifying existing PID entries
 * 
 * @author Thomas Eckart
 * @author Twan Goosen
 */
public class PidWriterImpl extends AbstractPidClient implements PidWriter {

	private final static Logger LOG = LoggerFactory.getLogger(PidWriterImpl.class);
	public static final Pattern PID_INPUT_PATTERN = Pattern.compile("^[0-9A-z-]+$");

	@Override
	public String registerNewPID(final Configuration configuration, Map<HandleField, String> fieldMap, String suffix)
			throws PidApiException, IllegalArgumentException {
		LOG.debug("Try to create handle {} at {} with values: {}", suffix, configuration.getServiceBaseURL(), fieldMap);

		// validate the requested PID
		if(!PID_INPUT_PATTERN.matcher(suffix).matches()) {
			throw new IllegalArgumentException("Pid suffix does not match input pattern: "+suffix);
		}

		final WebTarget webTarget = getWebtarget(configuration, String.format("%s/%s", configuration.getHandlePrefix(), suffix));
                final JSONArray jsonArray = createJSONArray(fieldMap);
                final Response response =  webTarget
                        .request(MediaType.APPLICATION_JSON)
                        .accept("application/json")
                        .header("If-None-Match", "*") // this header will tell the server to fail if the requested PID already exists
                        .put(Entity.entity(jsonArray.toString(), MediaType.APPLICATION_JSON));
                
		return processCreateResponse(response, configuration);
	}

	@Override
	public String registerNewPID(final Configuration configuration, Map<HandleField, String> fieldMap)
			throws PidApiException {
		LOG.debug("Try to create handle at {} with values: {}", configuration.getServiceBaseURL(), fieldMap);

                final WebTarget webTarget = getWebtarget(configuration,configuration.getHandlePrefix()); 
                final JSONArray jsonArray = createJSONArray(fieldMap);
                final Response response =  webTarget
                        .request(MediaType.APPLICATION_JSON)
                        .accept("application/json")
                        .post(Entity.entity(jsonArray.toString(), MediaType.APPLICATION_JSON));
                
                return processCreateResponse(response, configuration);
	}

        private String processCreateResponse(final Response response, final Configuration configuration)
			throws PidApiException, RuntimeException {
		if(response.getStatus() != 201) {
                    LOG.debug("Invalid API respose: code="+response.getStatus()+", message="+response.getStatusInfo().getReasonPhrase());
                    throw new PidApiException("Invalid API response" + response.getStatus());
		}

                String base = configuration.getServiceBaseURL();
                if(!base.endsWith("/")) {
                    base += "/";
                }

                String location = response.getLocation().toString().replace(base, "");
                LOG.debug("Registered new PID at location: "+location);
                return location;
	}

	@Override
	public void modifyPid(final Configuration configuration, final String pid, Map<HandleField, String> fieldMap) 
            throws PidApiException {
		LOG.debug("Try to modify handle \"" + pid + "\" at " + configuration.getServiceBaseURL() + " with new values: "
				+ fieldMap);

                final WebTarget webTarget = getWebtarget(configuration, pid);
		final JSONArray jsonArray = createJSONArray(fieldMap);
		 final Response response = webTarget
                    .request(MediaType.APPLICATION_JSON)
                    .accept("application/json")
                    .put(Entity.entity(jsonArray.toString(), MediaType.APPLICATION_JSON));
                 
                 if(response.getStatus() != 201) {
                     LOG.debug("Invalid API respose: code="+response.getStatus()+", message="+response.getStatusInfo().getReasonPhrase());
                     throw new PidApiException("Invalid API response: "+response.getStatus());
                 } else {
                     LOG.debug("Successfully modified PID: "+pid);
                 }
	}

	/**
	 * Generates JSON array that is understood by the EPIC handle service
	 * 
	 * @param fieldMap
	 *            mapping handle field to value
	 * @return JSON array
	 */
	private JSONArray createJSONArray(Map<HandleField, String> fieldMap) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;

		Iterator<HandleField> fieldIter = fieldMap.keySet().iterator();
		while (fieldIter.hasNext()) {
			jsonObject = new JSONObject();
			HandleField handleFieldTyp = fieldIter.next();
			jsonObject.put("type", handleFieldTyp.getType());
			jsonObject.put("parsed_data", fieldMap.get(handleFieldTyp));
			jsonArray.add(jsonObject);
		}

		return jsonArray;
	}
}