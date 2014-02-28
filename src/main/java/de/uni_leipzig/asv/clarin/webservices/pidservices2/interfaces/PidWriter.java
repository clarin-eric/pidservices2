package de.uni_leipzig.asv.clarin.webservices.pidservices2.interfaces;

import java.util.Map;

import org.apache.commons.httpclient.HttpException;

import de.uni_leipzig.asv.clarin.webservices.pidservices2.Configuration;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.HandleField;

/**
 * Registering new handles at handle server or modifying existing PID entries
 * 
 * @author Thomas Eckart
 * 
 */
public interface PidWriter {
	/**
	 * Try to register a new PID at handle server. Returns registered handle if successful.
	 * 
	 * @param configuration
	 * @param fieldMap
	 *            handle mapping field -> value
	 * @return registered handle identifier
	 * @throws HTTPException
	 */
	public String registerNewPID(final Configuration configuration, Map<HandleField, String> fieldMap)
			throws HttpException;

	/**
	 * Modify existing handle. Be aware that this method overwrites all existing fields! Fields that should remain stored in the PID have to be added to
	 * fieldMap.
	 * 
	 * @param configuration
	 * @param pid
	 *            Persistent identifier that will be modified/overwritten
	 * @param fieldMap
	 *            new field values: mapping handle field -> value
	 */
	public void modifyPid(final Configuration configuration, final String pid, Map<HandleField, String> fieldMap);
}
