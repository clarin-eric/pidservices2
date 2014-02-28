package de.uni_leipzig.asv.clarin.webservices.pidservices2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.impl.PidResolverImpl;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.interfaces.PidResolver;

public class PidResolverTest extends TestCase {
	private final String pid = "11022/0000-0000-1F9F-C";
	private final String url = "http://asv.informatik.uni-leipzig.de";

	public void testResolver() throws IOException {
		PidResolver resolver = new PidResolverImpl();
		PidObject pidObject = resolver.resolvePidAsPOJO(Configuration.getInstance(), pid);
		assertTrue(pidObject.getValue(HandleField.URL).equals(url));
	}

	public void testSearching() throws IOException {
		PidResolverImpl resolver = new PidResolverImpl();
		Map<HandleField, String> handleFieldMap = new HashMap<HandleField, String>();
		handleFieldMap.put(HandleField.URL, url);
		assertTrue(resolver.searchPidAsJSON(Configuration.getInstance(), handleFieldMap).keySet().size() > 0);
	}
}
