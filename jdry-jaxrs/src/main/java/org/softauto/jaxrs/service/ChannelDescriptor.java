package org.softauto.jaxrs.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.softauto.core.Configuration;
import org.softauto.core.vistors.builders.ClazzBuilder;
import org.softauto.jaxrs.JerseyClientFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.HashMap;


public class ChannelDescriptor extends AbstractHttp {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ChannelDescriptor.class);

    public static Builder newBuilder() { return new Builder();}

    public ChannelDescriptor(URI uri){
        this.uri = uri;
    }

    private URI uri;

    public URI getUri() {
       return uri;
    }



    public static class Builder{

        private String host;

        private int port;

        private String protocol;

        private String baseUrl;

        private  String path;

        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setProtocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public ChannelDescriptor build(Object[] args){
            URI uri = null;
            try {
                //uri = new URI(protocol + "://" + host + ":" + port +  path);
                if(!path.startsWith("/")){
                    path = "/" + path;
                }
                uri = UriBuilder.fromUri(protocol+"://"+host+":"+port+ path).build(args);
                //return new ChannelDescriptor(uri);
            }catch (Exception e){
                logger.error("fail create uri");
            }
            return new ChannelDescriptor(uri);
        }

    }

    /*
    public ChannelDescriptor build () throws Exception {
        client = new JerseyClientFactory().getClient();
        setHost(Configuration.get("jaxrs/host") != null ? Configuration.get("jaxrs/host").asText() : null);
        setPort(Configuration.get("jaxrs/port") != null ? Configuration.get("jaxrs/port").asInt(): null);
        setProtocol(Configuration.get("jaxrs/protocol") != null ? Configuration.get("jaxrs/protocol").asText() : null);
        setBaseUrl(Configuration.get("jaxrs/base_url") != null ? Configuration.get("jaxrs/base_url").asText(): null);
        JsonNode node = Configuration.get("jaxrs/headers");
        if(node != null && node.isContainerNode() && !node.isEmpty()) {
            String str = new ObjectMapper().writeValueAsString(node);
            HashMap<String, String> hm = new ObjectMapper().readValue(str, HashMap.class);
            headers = buildHeaders(hm);
        }
        return this;
    }

     */

}
