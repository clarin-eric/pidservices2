package de.uni_leipzig.asv.clarin.webservices.pidservices2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import junit.framework.TestCase;

import org.junit.Test;

public class ResolverTest extends TestCase {
	private static final String propFileName = "/config.properties";
	private static final String TEST_PID_PROPERTY = "TEST_PID";
	private static final String TEST_URL_PROPERTY = "TEST_URL";

	private Configuration configuration = null;
	private String testPid;
	private String testUrl;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		Properties properties = new Properties();
		BufferedInputStream propStream = new BufferedInputStream(getClass().getResourceAsStream(propFileName));
		properties.load(propStream);
		configuration = new Configuration(properties);
		testPid = properties.getProperty(TEST_PID_PROPERTY);
		testUrl = properties.getProperty(TEST_URL_PROPERTY);
	}

	public Configuration getConfiguration() throws IOException {
		return configuration;
	}

	@Test
	public void testPropertiesFileForExistence() {
		assertTrue((new File(getClass().getResource(propFileName).getFile())).exists());
	}

	public String getTestPid() {
		return testPid;
	}

	public String getTestUrl() {
		return testUrl;
	}
}
