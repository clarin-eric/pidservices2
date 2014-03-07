package de.uni_leipzig.asv.clarin.webservices.pidservices2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.impl.PidResolverImpl;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.impl.PidWriterImpl;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.interfaces.PidResolver;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.interfaces.PidWriter;

public class PidWriterTest extends TestCase {
	public void testPIDWriting() throws IOException {
		final String url = "http://asv.informatik.uni-leipzig.de";
		final String oldTitle = "Test Title";

		final PidWriter pidwriter = new PidWriterImpl();
		final PidResolver resolver = new PidResolverImpl();
		Map<HandleField, String> handleFieldMap;

		// // register new PID
		// handleFieldMap = new HashMap<HandleField, String>();
		// handleFieldMap.put(HandleField.URL, url);
		// handleFieldMap.put(HandleField.TITLE, oldTitle);
		// final String pid = pidwriter.registerNewPID(Configuration.getInstance(), handleFieldMap);

		// using existing PID to avoid creating a new one just for testing purposes
		final String pid = "11022/0000-0000-1F9F-C";

		// get original title
		PidObject pidObject = resolver.resolvePidAsPOJO(Configuration.getInstance(), pid);
		String title = pidObject.getValue(HandleField.TITLE);
		assertEquals(oldTitle, title);

		// set new title
		final String newTitle = "newtitle";
		handleFieldMap = new HashMap<HandleField, String>();
		handleFieldMap.put(HandleField.TITLE, newTitle);
		handleFieldMap.put(HandleField.URL, url);
		pidwriter.modifyPid(Configuration.getInstance(), pid, handleFieldMap);

		// check
		pidObject = resolver.resolvePidAsPOJO(Configuration.getInstance(), pid);
		final String changedTitle = pidObject.getValue(HandleField.TITLE);
		assertEquals(newTitle, changedTitle);

		// set old title again
		handleFieldMap.put(HandleField.TITLE, oldTitle);
		handleFieldMap.put(HandleField.URL, url);
		pidwriter.modifyPid(Configuration.getInstance(), pid, handleFieldMap);

		// check
		pidObject = resolver.resolvePidAsPOJO(Configuration.getInstance(), pid);
		title = pidObject.getValue(HandleField.TITLE);
		assertEquals(title, oldTitle);
	}
}
