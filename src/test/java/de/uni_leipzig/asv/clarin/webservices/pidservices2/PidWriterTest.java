package de.uni_leipzig.asv.clarin.webservices.pidservices2;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static de.uni_leipzig.asv.clarin.webservices.pidservices2.AbstractTest.TEST_API_PATH;
import static de.uni_leipzig.asv.clarin.webservices.pidservices2.AbstractTest.TEST_HANDLE_PREFIX;
import static de.uni_leipzig.asv.clarin.webservices.pidservices2.AbstractTest.TEST_HOST;
import static de.uni_leipzig.asv.clarin.webservices.pidservices2.AbstractTest.TEST_NEW_HANDLE_SUFFIX;
import static de.uni_leipzig.asv.clarin.webservices.pidservices2.AbstractTest.TEST_NEW_HANDLE_TITLE;
import static de.uni_leipzig.asv.clarin.webservices.pidservices2.AbstractTest.TEST_NEW_HANDLE_URL;
import static de.uni_leipzig.asv.clarin.webservices.pidservices2.AbstractTest.TEST_PORT;
import java.io.IOException;
import java.util.Map;
import org.junit.Test;

import de.uni_leipzig.asv.clarin.webservices.pidservices2.impl.PidResolverImpl;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.impl.PidWriterImpl;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.interfaces.PidResolver;
import de.uni_leipzig.asv.clarin.webservices.pidservices2.interfaces.PidWriter;
import java.util.EnumMap;
import net.sf.json.JSONObject;

public class PidWriterTest extends AbstractTest {

    final PidWriter pidwriter = new PidWriterImpl();
    final PidResolver resolver = new PidResolverImpl();

    protected String getJsonHandleField(HandleField type, String data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type.getType());
        jsonObject.put("parsed_data", data);
        return jsonObject.toString();
    }

    @Test
    public void testPIDWriting() throws IOException, PidApiException {
        stubFor(put(urlEqualTo(String.format("%s/%s/%s", TEST_API_PATH, TEST_HANDLE_PREFIX, TEST_NEW_HANDLE_SUFFIX)))
                .withBasicAuth(TEST_API_USER, TEST_API_PASSWORD)
                .withHeader("Accept", containing("application/json"))
                .withRequestBody(containing(getJsonHandleField(HandleField.URL, TEST_NEW_HANDLE_URL)))
                .withRequestBody(containing(getJsonHandleField(HandleField.TITLE, TEST_NEW_HANDLE_TITLE)))
                .willReturn(aResponse()
                        .withStatus(201)));

        final String pid = String.format("%s/%s", TEST_HANDLE_PREFIX, TEST_NEW_HANDLE_SUFFIX);
        final String newTitle = "newtitle";
        final Map<HandleField, String> handleFieldMap = new EnumMap<>(HandleField.class);
        handleFieldMap.put(HandleField.TITLE, TEST_NEW_HANDLE_TITLE);
        handleFieldMap.put(HandleField.URL, TEST_NEW_HANDLE_URL);

        pidwriter.modifyPid(configuration, pid, handleFieldMap);
    }

    @Test
    public void testCustomPIDWriting() throws Exception {
        stubFor(put(urlEqualTo(String.format("%s/%s/%s", TEST_API_PATH, TEST_HANDLE_PREFIX, TEST_NEW_HANDLE_SUFFIX)))
                .withBasicAuth(TEST_API_USER, TEST_API_PASSWORD)
                .withHeader("Accept", containing("application/json"))
                .withHeader("If-None-Match", containing("*"))
                .withRequestBody(containing(getJsonHandleField(HandleField.URL, TEST_NEW_HANDLE_URL)))
                .withRequestBody(containing(getJsonHandleField(HandleField.TITLE, TEST_NEW_HANDLE_TITLE)))
                .willReturn(aResponse()
                        .withHeader("Location",
                                String.format("http://%s:%d%s/%s/%s",
                                        TEST_HOST, TEST_PORT, TEST_API_PATH, TEST_HANDLE_PREFIX, TEST_NEW_HANDLE_SUFFIX))
                        .withStatus(201)));

        // register new PID
        final Map<HandleField, String> handleFieldMap = new EnumMap<>(HandleField.class);
        handleFieldMap.put(HandleField.URL, TEST_NEW_HANDLE_URL);
        handleFieldMap.put(HandleField.TITLE, TEST_NEW_HANDLE_TITLE);
        final String resultPid = pidwriter.registerNewPID(configuration, handleFieldMap, TEST_NEW_HANDLE_SUFFIX);
        final String expectedResultPid = configuration.getHandlePrefix() + "/" + TEST_NEW_HANDLE_SUFFIX;

        // test if requested ID was created
        assertEquals("Returned PID should equal requested PID", expectedResultPid, resultPid);
    }

    @Test
    public void testCustomPIDWritingIllegalPid() throws Exception {
        final String illegalPid = "This is not a valid PID";
        final Map<HandleField, String> handleFieldMap = new EnumMap<>(HandleField.class);

        try {
            // the following call should throw an exception
            pidwriter.registerNewPID(configuration, handleFieldMap, illegalPid);
            // we should not get here
            fail("Service accepted syntactically invalid PID");
        } catch (IllegalArgumentException ex) {
            // this should happen, pass test!
        }
    }
}
