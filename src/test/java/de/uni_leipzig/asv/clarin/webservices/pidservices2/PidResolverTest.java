package de.uni_leipzig.asv.clarin.webservices.pidservices2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.uni_leipzig.asv.clarin.webservices.pidservices2.impl.PidResolverImpl;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.interfaces.PidResolver;

public class PidResolverTest extends ResolverTest {
	@Test
	public void testResolver() throws IOException {
		PidResolver resolver = new PidResolverImpl();
		PidObject pidObject = resolver.resolvePidAsPOJO(getConfiguration(), getTestPid());
		assertTrue(pidObject.getValue(HandleField.URL).equals(getTestUrl()));
	}

	@Test
	public void testSearchingList() throws IOException {
		PidResolver resolver = new PidResolverImpl();
		Map<HandleField, String> handleFieldMap = new HashMap<HandleField, String>();
		handleFieldMap.put(HandleField.URL, getTestUrl());
		assertTrue(resolver.searchPidAsList(getConfiguration(), handleFieldMap).size() > 0);
	}

	@Test
	public void testSearchingJSON() throws IOException {
		PidResolver resolver = new PidResolverImpl();
		Map<HandleField, String> handleFieldMap = new HashMap<HandleField, String>();
		handleFieldMap.put(HandleField.URL, getTestUrl());
		assertTrue(resolver.searchPidAsJSON(getConfiguration(), handleFieldMap).keySet().size() > 0);
	}
}
