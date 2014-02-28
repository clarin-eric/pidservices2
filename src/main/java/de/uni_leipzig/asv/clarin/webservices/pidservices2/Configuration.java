package de.uni_leipzig.asv.clarin.webservices.pidservices2;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Stores some information needed for establishing connection to resolver server
 * 
 * @author Thomas Eckart
 * 
 */
public class Configuration {
	private final static Logger LOG = Logger.getLogger(Configuration.class);
	private static Configuration instance = null;

	private String serviceBaseURL;
	private String handlePrefix;
	private String user;
	private String password;

	/**
	 * @return Singleton
	 */
	public static Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration();
		}
		return instance;
	}

	/**
	 * private constructor for Singleton
	 */
	private Configuration() {
		final Properties properties = new Properties();
		try {
			final BufferedInputStream stream = new BufferedInputStream(new FileInputStream("config.properties"));
			properties.load(stream);
			stream.close();

			serviceBaseURL = properties.getProperty("SERVICE_BASE_URL");
			handlePrefix = properties.getProperty("HANDLE_PREFIX");
			user = properties.getProperty("USER");
			password = properties.getProperty("PASSWORD");
		} catch (final Exception e) {
			LOG.debug("Could not read parameters from file config.properties - using default values");
			serviceBaseURL = "http://pid.gwdg.de/handles/";
			handlePrefix = "11022";
			user = "1005-01";
			password = "";
		}
	}

	/**
	 * constructor
	 * 
	 * @param serviceBaseURL
	 * @param handlePrefix
	 * @param user
	 * @param password
	 */
	public Configuration(final String serviceBaseURL, final String handlePrefix, final String user,
			final String password) {
		this.serviceBaseURL = serviceBaseURL;
		this.handlePrefix = handlePrefix;
		this.user = user;
		this.password = password;
	}

	public static void setInstance(final Configuration instance) {
		Configuration.instance = instance;
	}

	public void setServiceBaseURL(final String serviceBaseURL) {
		this.serviceBaseURL = serviceBaseURL;
	}

	public void setHandlePrefix(final String handlePrefix) {
		this.handlePrefix = handlePrefix;
	}

	public void setUser(final String user) {
		this.user = user;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * @return serviceBaseURL (e.g. http://handle.gwdg.de:8080/pidservice/)
	 */
	public String getServiceBaseURL() {
		return serviceBaseURL;
	}

	/**
	 * @return handle prefix (e.g. 11022)
	 */
	public String getHandlePrefix() {
		return handlePrefix;
	}

	/**
	 * @return resolver account name
	 */
	public String getUser() {
		return user;
	}

	/**
	 * 
	 * @return resolver password
	 */
	public String getPassword() {
		return password;
	}
}
