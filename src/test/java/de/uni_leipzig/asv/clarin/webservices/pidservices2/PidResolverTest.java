package de.uni_leipzig.asv.clarin.webservices.pidservices2;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import de.uni_leipzig.asv.clarin.webservices.pidservices2.impl.PidResolverImpl;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.interfaces.PidResolver;
import java.util.EnumMap;

public class PidResolverTest extends AbstractTest {
    
    @Test
    public void testResolver() throws IOException {  
        PidResolver resolver = new PidResolverImpl();
        PidObject pidObject = resolver.resolvePidAsPOJO(configuration, testPid);
        assertTrue(pidObject.getValue(HandleField.URL).equals(testUrl));
    }

    @Test
    public void testSearchingList() throws IOException {        
        PidResolver resolver = new PidResolverImpl();
        Map<HandleField, String> handleFieldMap = new EnumMap<>(HandleField.class);
        handleFieldMap.put(HandleField.URL, testUrl);
        assertTrue(resolver.searchPidAsList(configuration, handleFieldMap).size() > 0);
        assertTrue(resolver.searchPidAsList(configuration, handleFieldMap).get(0).equalsIgnoreCase(testPid));
    }

    @Test
    public void testSearchingJSON() throws IOException {
        PidResolver resolver = new PidResolverImpl();
        Map<HandleField, String> handleFieldMap = new EnumMap<>(HandleField.class);
        handleFieldMap.put(HandleField.URL, testUrl);
        assertTrue(resolver.searchPidAsJSON(configuration, handleFieldMap).keySet().size() > 0);
    }
}
