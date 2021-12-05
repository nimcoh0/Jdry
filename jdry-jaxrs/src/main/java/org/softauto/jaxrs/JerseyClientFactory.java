package org.softauto.jaxrs;

import org.softauto.core.Configuration;
import org.softauto.core.Utils;
import org.softauto.jaxrs.auto.Basic;
import org.softauto.jaxrs.auto.None;
import org.softauto.jaxrs.auto.Ssl;
import org.softauto.jaxrs.auto.SslSelfSigned;

/**
 * jaxrs client Authentication method Factory
 */
public class JerseyClientFactory {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(JerseyClientFactory.class);

    public JerseyClientFactory(){

    }

    public javax.ws.rs.client.Client getClient() throws Exception{
        if(Utils.getParam(Configuration.get("jaxrs"),"auth") == null){
            return new None().getClient();
        }
            switch (Utils.getParam(Configuration.get("jaxrs"),"auth")) {
                case "BASIC": return  new Basic().getClient();
                case "NONE" : return  new None().getClient();
                case "SSL"  : return new Ssl().getClient();
                case "SSL_SELF_SIGNED" : return new SslSelfSigned().getClient();
                default: logger.error("auth type " + Configuration.get("jaxrs/auth") + " not supported");
             }
        return null;
    }


}
