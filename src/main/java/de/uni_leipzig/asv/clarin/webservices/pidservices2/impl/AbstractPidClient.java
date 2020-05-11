package de.uni_leipzig.asv.clarin.webservices.pidservices2.impl;

import de.uni_leipzig.asv.clarin.webservices.pidservices2.Configuration;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wilelb
 */
public abstract class AbstractPidClient {
    private final static Logger LOG = LoggerFactory.getLogger(PidResolverImpl.class);

    protected WebTarget getWebtarget(final Configuration configuration, final String path) {
        final HttpAuthenticationFeature feature = 
                HttpAuthenticationFeature.basic(configuration.getUser(), configuration.getPassword());
        final Client client = ClientBuilder.newClient();
        client.register(feature);		
        return client.target(configuration.getServiceBaseURL()).path(path);
    }
}
