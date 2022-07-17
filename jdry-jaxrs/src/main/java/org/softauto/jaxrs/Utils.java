package org.softauto.jaxrs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.Protocol;
import org.apache.avro.Schema;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.softauto.core.Configuration;
import org.softauto.jaxrs.service.ChannelDescriptor;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utils {

    public static Entity buildEntity(String produces, Object[] args){
        Entity<?> entity = null;
        try{
               if (produces != null && produces.equals(MediaType.MULTIPART_FORM_DATA_TYPE)) {
                    Form form = new Form();
                    for (int i = 0; i < args.length; i++) {
                        String json = new ObjectMapper().writeValueAsString(args[i]);
                        form.param(args[i].getClass().getName(), json);
                    }

                    entity = Entity.entity(form, MediaType.MULTIPART_FORM_DATA_TYPE);
                }
                if (produces != null && produces.equals(MediaType.APPLICATION_FORM_URLENCODED)) {
                    Form form = new Form();
                    for (int i = 0; i < args.length; i++) {
                        String json = new ObjectMapper().writeValueAsString(args[i]);
                        form.param(args[i].getClass().getName(), json);
                    }
                    entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);
                }
                if (produces != null &&  produces.equals(MediaType.APPLICATION_JSON)) {
                    entity = Entity.entity(args[0],MediaType.APPLICATION_JSON);
                }
         }catch (Exception e){
            e.printStackTrace();
        }
        return entity;
    }


    public static void addProperties(HashMap<String,Object> properties, Client client){
        properties.forEach((k,v)->{
            client.property(k,v);
        });

    }


    public static javax.ws.rs.core.MediaType getProduce(HashMap<String,Object> props){
        HashMap<String,Object> annotations = (HashMap<String, Object>) props.get("annotations");
        if(annotations.containsKey("javax.ws.rs.Produces")){
            String value = ((ArrayList<String>)((HashMap<String,Object>)annotations.get("javax.ws.rs.Produces")).get("value")).get(0).toString();
            return javax.ws.rs.core.MediaType.valueOf(value);
        }
        return null;
    }

    public static String getPath(HashMap<String,Object> props){
        HashMap<String,Object> annotations = (HashMap<String, Object>) props.get("annotations");
        if(annotations.containsKey("javax.ws.rs.Path")){
            return ((HashMap<String,Object>)annotations.get("javax.ws.rs.Path")).get("value").toString();
        }
        return null;
    }

    public static String gethttpMethod(HashMap<String,Object> props){
        HashMap<String,Object> annotations = (HashMap<String, Object>) props.get("annotations");
        if(annotations.containsKey("javax.ws.rs.POST")){
            return HttpMethod.POST;
        }
        if(annotations.containsKey("javax.ws.rs.GET")){
            return HttpMethod.GET;
        }
        if(annotations.containsKey("javax.ws.rs.PUT")){
            return HttpMethod.PUT;
        }
        if(annotations.containsKey("javax.ws.rs.DELETE")){
            return HttpMethod.DELETE;
        }
        return null;
    }

    public static Client getClient(HashMap<String,Object> callOptions){
        Client client = null;
        if(((HashMap<String,Object>)callOptions.get("authentication")).get("schema") != null) {
            String schema = ((HashMap<String,Object>)callOptions.get("authentication")).get("schema").toString();
            String username = ((HashMap<String,Object>)callOptions.get("authentication")).get("username").toString();
            String password = ((HashMap<String,Object>)callOptions.get("authentication")).get("password").toString();
            if(schema.equals("Basic")) {
                HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);
                client = ClientBuilder.newBuilder().register(feature).build();
            }
        }else {
            client = ClientBuilder.newBuilder().build();
        }
        return client;
    }

    public static ChannelDescriptor getChannel(String path,Object[] args){
        return ChannelDescriptor.newBuilder()
                .setHost(((HashMap<String,Object>) Configuration.get("jaxrs")).get("host").toString())
                .setPort(Integer.valueOf(((HashMap<String,Object>)Configuration.get("jaxrs")).get("port").toString()))
                .setProtocol(((HashMap<String,Object>)Configuration.get("jaxrs")).get("protocol").toString())
                .setBaseUrl(((HashMap<String,Object>)Configuration.get("jaxrs")).get("base_url").toString())
                .setPath(path)
                .build((Object[]) args);
    }
}
