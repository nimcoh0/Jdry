package org.softauto.jaxrs.auto;

import javax.ws.rs.client.ClientBuilder;

/**
 * create jaxrs client without Authentication
 */
public class None {

    public javax.ws.rs.client.Client getClient() throws Exception {
        return ClientBuilder.newClient();
    }

}
