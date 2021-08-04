package org.softauto.jaxrs.service;


import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Map;

public class AbstractHttp {


    protected URI buildUrl(String url,  String baseUrl , Object[] args){
        if(!url.startsWith("/")){
            url = "/"+ url;
        }
        if(args != null) {
            return UriBuilder.fromUri(baseUrl + url).build(args);
        }else {
            return UriBuilder.fromUri(baseUrl + url).build();
        }
    }

    protected MultivaluedMap<String, Object> buildHeaders(Map<String, String> header) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        header.forEach((k, v) -> {
            headers.add(k, v);
        });
        return headers;
    }


}
