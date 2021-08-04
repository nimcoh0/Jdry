package org.softauto.jaxrs.auto;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.softauto.core.Configuration;

import javax.ws.rs.client.ClientBuilder;

/**
 * create jaxrs client with Basic Authentication
 */
public class Basic {


    public javax.ws.rs.client.Client getClient() throws Exception {
        return createClientBasicAuthentication( Configuration.get("jaxrs/username").asText(),Configuration.get("jaxrs/password").asText());
    }

    public javax.ws.rs.client.Client createClientBasicAuthentication(String username,String password) throws Exception {
        javax.ws.rs.client.Client client =  ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);
        client.register(feature);
        return client;
    }

}
