package de.uni_leipzig.asv.clarin.webservices.pidservices2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.uni_leipzig.asv.clarin.webservices.pidservices2.impl.PidResolverImpl;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.interfaces.PidResolver;

public class PidResolverTest extends ResolverTest {
	private final String pid = "11022/0000-0000-1F9F-C";
	private final String url = "http://asv.informatik.uni-leipzig.de";

	@Test
	public void testResolver() throws IOException {
		PidResolver resolver = new PidResolverImpl();
		PidObject pidObject = resolver.resolvePidAsPOJO(getConfiguration(), pid);
		assertTrue(pidObject.getValue(HandleField.URL).equals(url));
	}

	@Test
	public void testSearchingList() throws IOException {
		PidResolver resolver = new PidResolverImpl();
		Map<HandleField, String> handleFieldMap = new HashMap<HandleField, String>();
		handleFieldMap.put(HandleField.URL, url);
		assertTrue(resolver.searchPidAsList(getConfiguration(), handleFieldMap).size() > 0);
	}

	@Test
	public void testSearchingJSON() throws IOException {
		PidResolver resolver = new PidResolverImpl();
		Map<HandleField, String> handleFieldMap = new HashMap<HandleField, String>();
		handleFieldMap.put(HandleField.URL, url);
		assertTrue(resolver.searchPidAsJSON(getConfiguration(), handleFieldMap).keySet().size() > 0);
	}
}
