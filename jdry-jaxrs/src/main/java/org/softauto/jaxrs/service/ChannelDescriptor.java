package org.softauto.jaxrs.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.softauto.core.Configuration;
import org.softauto.jaxrs.JerseyClientFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MultivaluedMap;
import java.net.URI;
import java.util.HashMap;


public class ChannelDescriptor extends AbstractHttp {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ChannelDescriptor.class);

    private String host;

    private int port;

    private String protocol;

    private Client client;

    private MultivaluedMap<String, Object> headers;

    private  Object[] args;

    private  String path;

    private URI uri;

    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public URI getUri(Object[] args) {
       return buildUrl(path, baseUrl,  args);

    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public MultivaluedMap<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(MultivaluedMap<String, Object> headers) {
        this.headers = headers;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getPath() {
        return path;
    }

    public ChannelDescriptor setPath(String path) {
        this.path = path;
        return this;
    }



    public ChannelDescriptor build () throws Exception {
        client = new JerseyClientFactory().getClient();
        setHost(Configuration.get("jaxrs/httpHost").asText());
        setPort(Configuration.get("jaxrs/httpPort").asInt());
        setProtocol(Configuration.get("jaxrs/protocol").asText());
        setBaseUrl(Configuration.get("jaxrs/base_url").asText());
        JsonNode node = Configuration.get("jaxrs/headers");
        if(node != null && node.isContainerNode() && !node.isEmpty()) {
            String str = new ObjectMapper().writeValueAsString(node);
            HashMap<String, String> hm = new ObjectMapper().readValue(str, HashMap.class);
            headers = buildHeaders(hm);
        }
        return this;
    }

}
