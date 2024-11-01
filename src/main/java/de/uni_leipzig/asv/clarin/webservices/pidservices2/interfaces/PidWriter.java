package de.uni_leipzig.asv.clarin.webservices.pidservices2.interfaces;

import java.util.Map;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.Configuration;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.HandleField;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.PidApiException;

/**
 * Registers new handles at handle server or modifies existing handles
 * 
 * @author Thomas Eckart
 * @author Twan Goosen
 */
public interface PidWriter {
	/**
	 * Try to register a new PID at handle server. Returns registered handle if successful.
	 * 
	 * @param configuration
	 * @param fieldMap
	 * @param pid
	 *            PID to be created, must match (see PID_INPUT_PATTERN)
	 * @return registered handle identifier
         * @throws de.uni_leipzig.asv.clarin.webservices.pidservices2.PidApiException
	 * @throws IllegalArgumentException
	 *             if the provided PID does not match (see PID_INPUT_PATTERN)
	 */
	public String registerNewPID(final Configuration configuration, Map<HandleField, String> fieldMap, String pid)
			throws PidApiException;

	/**
	 * Try to register a new PID at handle server.Returns registered handle if successful.
	 * 
	 * @param configuration
	 * @param fieldMap
	 *            handle mapping field to value
	 * @return registered handle identifier
         * @throws de.uni_leipzig.asv.clarin.webservices.pidservices2.PidApiException
	 * @throws HTTPException
	 */
	public String registerNewPID(final Configuration configuration, Map<HandleField, String> fieldMap)
			throws PidApiException;

	/**
	 * Modify existing PID.This method overwrites all existing fields! Fields that should remain stored for the PID have to be added to fieldMap.
	 * 
	 * @param configuration
	 * @param pid
	 *            Persistent identifier that will be modified/overwritten
	 * @param fieldMap
	 *            new field values: mapping handle field to value
         * @throws de.uni_leipzig.asv.clarin.webservices.pidservices2.PidApiException
	 */
	public void modifyPid(final Configuration configuration, final String pid, Map<HandleField, String> fieldMap)
                throws PidApiException;
}
