package de.uni_leipzig.asv.clarin.webservices.pidservices2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_leipzig.asv.clarin.webservices.pidservices2.impl.PidResolverImpl;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.interfaces.PidResolver;

public class PidResolverTest extends TestCase {
	private final String pid = "11022/0000-0000-1F9F-C";
	private final String url = "http://asv.informatik.uni-leipzig.de";
	private Configuration configuration;

	private final static Logger LOG = LoggerFactory.getLogger(PidResolverTest.class);

	public PidResolverTest() {
		try {
			configuration = new Configuration();
		} catch (IOException e) {
			configuration = null;
			LOG.error(e.getMessage());
		}
	}

	public void testResolver() throws IOException {
		PidResolver resolver = new PidResolverImpl();
		PidObject pidObject = resolver.resolvePidAsPOJO(configuration, pid);
		assertTrue(pidObject.getValue(HandleField.URL).equals(url));
	}

	public void testSearchingList() throws IOException {
		PidResolver resolver = new PidResolverImpl();
		Map<HandleField, String> handleFieldMap = new HashMap<HandleField, String>();
		handleFieldMap.put(HandleField.URL, url);
		assertTrue(resolver.searchPidAsList(configuration, handleFieldMap).size() > 0);
	}

	public void testSearchingJSON() throws IOException {
		PidResolver resolver = new PidResolverImpl();
		Map<HandleField, String> handleFieldMap = new HashMap<HandleField, String>();
		handleFieldMap.put(HandleField.URL, url);
		assertTrue(resolver.searchPidAsJSON(configuration, handleFieldMap).keySet().size() > 0);
	}
}
