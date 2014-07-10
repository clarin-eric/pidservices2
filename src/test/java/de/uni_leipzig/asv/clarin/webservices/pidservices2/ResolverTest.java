package de.uni_leipzig.asv.clarin.webservices.pidservices2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import junit.framework.TestCase;

import org.junit.Test;

public class ResolverTest extends TestCase {
	private final String propFileName = "/config.properties";
	private Configuration configuration = null;

	public Configuration getConfiguration() throws IOException {
		if (configuration == null) {
			Properties properties = new Properties();
			BufferedInputStream propStream = new BufferedInputStream(new FileInputStream(getClass().getResource(
					propFileName).getFile()));
			properties.load(propStream);
			configuration = new Configuration(properties);
		}
		return configuration;
	}

	@Test
	public void testPropertiesFileForExistence() {
		assertTrue((new File(getClass().getResource(propFileName).getFile())).exists());
	}
}
