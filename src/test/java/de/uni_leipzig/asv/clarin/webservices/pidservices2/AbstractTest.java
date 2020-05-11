package de.uni_leipzig.asv.clarin.webservices.pidservices2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import java.util.Properties;
import junit.framework.TestCase;

public abstract class AbstractTest extends TestCase {        
    protected static final int TEST_PORT = 8089;
    protected static final String TEST_HOST = "localhost";
    protected static final String TEST_API_USER = "theusername";
    protected static final String TEST_API_PASSWORD = "thepassword";
    protected static final String TEST_API_PATH = "/handles";
    protected static final String TEST_HANDLE_PREFIX = "12345";
    protected static final String TEST_HANDLE_SUFFIX = "test-sufix";
    protected static final String TEST_HANDLE_URL_VALUE = "http://www.test.eu";

     protected static final String TEST_NEW_HANDLE_SUFFIX = "test-sufix-new1";
     protected static final String TEST_NEW_HANDLE_URL = "http://new.test.eu";
     protected static final String TEST_NEW_HANDLE_TITLE = "new title";
     
    protected Configuration configuration = null;
    protected String testPid;
    protected String testUrl;

    private final WireMockServer wireMockServer = new WireMockServer(TEST_PORT);

    private final ObjectMapper mapper = new ObjectMapper();
    
    @Override
    public void setUp() throws Exception {
        super.setUp();

        //Configure the client
        Properties properties = new Properties();
        properties.put("SERVICE_BASE_URL", String.format("http://%s:%d%s", TEST_HOST, TEST_PORT, TEST_API_PATH));
        properties.put("HANDLE_PREFIX", TEST_HANDLE_PREFIX);
        properties.put("USER", TEST_API_USER);
        properties.put("PASSWORD", TEST_API_PASSWORD);
        configuration = new Configuration(properties);

        testPid = String.format("%s/%s", TEST_HANDLE_PREFIX, TEST_HANDLE_SUFFIX);
        testUrl = TEST_HANDLE_URL_VALUE;

        //Start the mock API server
        wireMockServer.start();

        //Mock the API calls        
        configureFor(TEST_HOST, TEST_PORT);
        stubFor(get(urlEqualTo(String.format("%s/%s/?%s", TEST_API_PATH, TEST_HANDLE_PREFIX, "URL=http%3A%2F%2Fwww.test.eu")))
            .withBasicAuth(TEST_API_USER, TEST_API_PASSWORD)
            .willReturn(aResponse()
                .withStatus(200)
                .withBody(mapper.writeValueAsString(
                        new String[] {TEST_HANDLE_SUFFIX}
                ))));
        
        
       stubFor(get(urlEqualTo(String.format("%s/%s", TEST_API_PATH, testPid)))
            .withBasicAuth(TEST_API_USER, TEST_API_PASSWORD)
            .willReturn(aResponse()
                .withStatus(200)
                .withBody(mapper.writeValueAsString(
                        new PidField[] {new PidField(1, "URL", TEST_HANDLE_URL_VALUE)}
                ))));
       
        /*
       stubFor(put(urlEqualTo(String.format("%s/%s/%s", TEST_API_PATH, TEST_HANDLE_PREFIX, TEST_NEW_HANDLE_SUFFIX)))
            .withBasicAuth(TEST_API_USER, TEST_API_PASSWORD)
            .withHeader("Accept", containing("application/json"))
            .withRequestBody(containing("\"type\": \""+HandleField.URL+"\""))
            .withRequestBody(containing("\"parsed_data\": \""+TEST_NEW_HANDLE_URL+"\""))
            .withRequestBody(containing("\"type\": \""+HandleField.TITLE+"\""))
            .withRequestBody(containing("\"parsed_data\": \""+TEST_NEW_HANDLE_TITLE+"\""))
            .willReturn(aResponse()
                .withStatus(201)));
       */
       /*
       JSONArray jsonArray = new JSONArray();
       JSONObject jsonObject = new JSONObject();
       jsonObject.put("type", HandleField.URL.getType());
       jsonObject.put("parsed_data", TEST_NEW_HANDLE_URL);
       jsonArray.add(jsonObject);
       jsonObject = new JSONObject();
       jsonObject.put("type", HandleField.TITLE.getType());
       jsonObject.put("parsed_data", TEST_NEW_HANDLE_TITLE);
       jsonArray.add(jsonObject);
       
       stubFor(put(urlEqualTo(String.format("%s/%s/%s", TEST_API_PATH, TEST_HANDLE_PREFIX, TEST_NEW_HANDLE_SUFFIX)))
            .withBasicAuth(TEST_API_USER, TEST_API_PASSWORD)
            .withHeader("Accept", containing("application/json"))
            .withHeader("If-None-Match", containing("*"))
            .withRequestBody(containing(jsonArray.toString()))
            .willReturn(aResponse()
                .withHeader("Location", 
                    String.format("http://%s:%d%s/%s/%s", 
                    TEST_HOST, TEST_PORT, TEST_API_PATH, TEST_HANDLE_PREFIX, TEST_NEW_HANDLE_SUFFIX))
                .withStatus(201)));
       */
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        wireMockServer.stop();
    }
        
    private class PidField {
        public int idx;
        public String type;
        public String parsed_data;
        public String data;
        public String timestamp;
        public int ttl_type;
        public int ttl;
        public String[] refs;
        public String privs;       
        
        public PidField(int idx, String type, String data) {
            this.idx = idx;
            this.type = type;
            this.parsed_data = data;
            //this.data = 
            this.timestamp = "2013-11-26T11:58:14Z";
            this.ttl = 86400;
            this.ttl_type = 0;
            this.refs = new String[] {};
            this.privs = "rwr-";
                    
        }
    }
}