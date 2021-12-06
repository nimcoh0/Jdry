package org.softauto.jaxrs.service;


import org.apache.avro.Protocol;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.softauto.core.Configuration;
import org.softauto.core.Utils;
import org.softauto.jaxrs.JerseyClientFactory;
import org.softauto.jaxrs.JerseyHelper;
import org.softauto.jaxrs.Options;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;


public class RestService {

    private static ServiceDefinition serviceDefinition = null;
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(RestService.class);

    public static ServiceDefinition getServiceDefinition(){
         return serviceDefinition;
    }

    public static ServiceDefinition createServiceDefinition(Class iface) {
        ServiceDefinition.Builder serviceDefinitionBuilder = null;
        try {
            Protocol protocol = Utils.getProtocol(iface);
            Map<String, Protocol.Message> messages = protocol.getMessages();
            ServiceDescriptor serviceDescriptor = ServiceDescriptor.create(iface);
            serviceDefinitionBuilder = ServiceDefinition.builder(serviceDescriptor);
           for (Method method : iface.getMethods()) {
                Protocol.Message message = messages.get(method.getName());
                if (message.getProp("transceiver").equals("JAXRS")) {
                    if (message != null) {
                        Map<String, Object> msg = message.getObjectProps();
                        Map<String, Object> jaxrs = (Map<String, Object>) msg.get("jaxrs");
                        //ChannelDescriptor channel = ChannelDescriptor.newBuilder()
                                //.setHost(Configuration.get("jaxrs/host").asText())
                                //.setPort(Configuration.get("jaxrs/port").asInt())
                                //.setProtocol(Configuration.get("jaxrs/protocol").asText())
                                //.setPath(jaxrs.get("Path").toString())
                                //.build();
                        String httpMethod = jaxrs.get("HttpMethod").toString();
                        if (httpMethod.equals("GET")) {
                            serviceDefinitionBuilder.addMethod(serviceDescriptor.getMethods(method, message, MethodDescriptor.MethodType.GET),
                                    ServiceCaller.call(new GETMethodHandler()), msg);
                        }
                        if (httpMethod.equals("PUT")) {
                            serviceDefinitionBuilder.addMethod(serviceDescriptor.getMethods(method, message, MethodDescriptor.MethodType.PUT),
                                    ServiceCaller.call(new PUTMethodHandler()), msg);
                        }
                        if (httpMethod.equals("POST")) {
                            serviceDefinitionBuilder.addMethod(serviceDescriptor.getMethods(method, message, MethodDescriptor.MethodType.POST),
                                    ServiceCaller.call(new POSTMethodHandler()), msg);
                        }
                        if (httpMethod.equals("DELETE")) {
                            serviceDefinitionBuilder.addMethod(serviceDescriptor.getMethods(method, message, MethodDescriptor.MethodType.DELETE),
                                    ServiceCaller.call(new DELETEMethodHandler()), msg);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return serviceDefinition = serviceDefinitionBuilder.build();
    }

    private static class GETMethodHandler implements ServiceCaller.UnaryClass  {

        @Override
        public <T> T invoke(MethodDescriptor methodDescriptor,Object[] args,Map<String, Object> msg,Class<T> res) {
            try {
                //Client client = new JerseyClientFactory().getClient();
                HashMap<String,Object> callOptions = (HashMap<String, Object>) args[2];
                Client client = null;
                if(callOptions.get("feature") != null) {
                    client = ClientBuilder.newBuilder().register((HttpAuthenticationFeature) callOptions.get("feature")).build();
                }else {
                    client = ClientBuilder.newBuilder().build();
                }
                org.softauto.jaxrs.Utils.addProperties((HashMap<String, Object>) callOptions.get("properties"),client);
                //Class[] types = (Class[])args[1];
                //Object[] arguments = (Object[])args[0];
                MultivaluedMap<String, Object> headers = (MultivaluedMap<String, Object>)callOptions.get(Options.headers.name());
                //Entity<?> entity = (Entity<?>)callOptions.get(Options.entity.name());
                String produces = callOptions.get(Options.produce.name()).toString();
                //Class<T> response = (Class)callOptions.get(Options.response.name());
                Map<String, Object> jaxrs = (Map<String, Object>) msg.get("jaxrs");
                ChannelDescriptor channel = ChannelDescriptor.newBuilder()
                        .setHost(Configuration.get("jaxrs/host").asText())
                        .setPort(Configuration.get("jaxrs/port").asInt())
                        .setProtocol(Configuration.get("jaxrs/protocol").asText())
                        .setPath(jaxrs.get("base_url")== null ? jaxrs.get("Path").toString():jaxrs.get("base_url").toString()+jaxrs.get("Path").toString() )
                        .build((Object[]) args[0]);
               // MultivaluedMap<String, Object> headers = HeaderBuilder.newBuilder().setHeader(Configuration.get("jaxrs/headers")).build().getHeaders();
                URI uri =  channel.getUri();
                //URI uri = channel.setPath(methodDescriptor.getPath()).getUri((Object[])args[0]);
                logger.debug("invoke GET for "+ uri);
                //return new JerseyHelper(client).get(uri.toString(), methodDescriptor.getProduces(), headers, res);
                return new JerseyHelper(client).get(uri.toString(), produces, headers, res);
            }catch (Exception e){
                logger.error("fail invoke GET for uri "+ methodDescriptor.getPath()+ " with args "+ Utils.result2String((Object[])args[0]),e);
            }
            return null;
        }
    }

    private static class POSTMethodHandler implements ServiceCaller.UnaryClass  {

        @Override
        public <T> T invoke(MethodDescriptor methodDescriptor,Object[] args,Map<String, Object> msg,Class<T> res) {
            try {

                HashMap<String,Object> callOptions = (HashMap<String, Object>) args[2];
                Client client = null;
                if(callOptions.get("feature") != null) {
                    client = ClientBuilder.newBuilder().register((HttpAuthenticationFeature) callOptions.get("feature")).build();
                }else {
                    client = ClientBuilder.newBuilder().build();
                }
                org.softauto.jaxrs.Utils.addProperties((HashMap<String, Object>) callOptions.get("properties"),client);
                        //new JerseyClientFactory().getClient().register((HttpAuthenticationFeature)callOptions.get("feature"));
                //Class[] types = (Class[])args[1];
                //Object[] arguments = (Object[])args[0];
                MultivaluedMap<String, Object> headers = (MultivaluedMap<String, Object>)callOptions.get(Options.headers.name());
                //Entity<?> entity = (Entity<?>)callOptions.get(Options.entity.name());
                String produces = callOptions.get(Options.produce.name()).toString();
                //Class<T> response = (Class)callOptions.get(Options.response.name());
                //MultivaluedMap<String, Object> headers = HeaderBuilder.newBuilder().setHeader(Configuration.get("jaxrs/headers")).build().getHeaders();
                //URI uri = channel.setPath(methodDescriptor.getPath()).getUri((Object[])args[0]);
                Map<String, Object> jaxrs = (Map<String, Object>) msg.get("jaxrs");
                ChannelDescriptor channel = ChannelDescriptor.newBuilder()
                        .setHost(Configuration.get("jaxrs/host").asText())
                        .setPort(Configuration.get("jaxrs/port").asInt())
                        .setProtocol(Configuration.get("jaxrs/protocol").asText())
                        .setPath(jaxrs.get("Path").toString())
                        .build((Object[]) args[0]);
                URI uri =  channel.getUri();
                Entity<?> entity = org.softauto.jaxrs.Utils.buildEntity(produces,(Object[])args[0]);
                logger.debug("invoke POST for "+ uri + " with headers "+ headers.values() + " entity");
                return new JerseyHelper(client).post(uri.toString(), produces, headers, res,entity);

            }catch (Exception e){
                logger.error("fail invoke POST for uri "+ methodDescriptor.getPath()+ " with args "+ Utils.result2String((Object[])args[0]),e);
            }
            return null;
        }
    }

    private static class PUTMethodHandler implements ServiceCaller.UnaryClass  {

        @Override
        public <T> T invoke(MethodDescriptor methodDescriptor,Object[] args,Map<String, Object> msg,Class<T> res ){
            try {
                //Client client = new JerseyClientFactory().getClient();
                HashMap<String,Object> callOptions = (HashMap<String, Object>) args[2];
                Client client = null;
                if(callOptions.get("feature") != null) {
                    client = ClientBuilder.newBuilder().register((HttpAuthenticationFeature) callOptions.get("feature")).build();
                }else {
                    client = ClientBuilder.newBuilder().build();
                }
                org.softauto.jaxrs.Utils.addProperties((HashMap<String, Object>) callOptions.get("properties"),client);
                Class[] types = (Class[])args[1];
                Object[] arguments = (Object[])args[0];
                MultivaluedMap<String, Object> headers = (MultivaluedMap<String, Object>)callOptions.get(Options.headers.name());
                //Entity<?> entity = (Entity<?>)callOptions.get(Options.entity.name());
                String produces = callOptions.get(Options.produce.name()).toString();
                //Class<T> response = (Class)callOptions.get(Options.response.name());
               //MultivaluedMap<String, Object> headers = HeaderBuilder.newBuilder().setHeader(Configuration.get("jaxrs/headers")).build().getHeaders();
                //URI uri = channel.setPath(methodDescriptor.getPath()).getUri((Object[])args[0]);
                Map<String, Object> jaxrs = (Map<String, Object>) msg.get("jaxrs");
                ChannelDescriptor channel = ChannelDescriptor.newBuilder()
                        .setHost(Configuration.get("jaxrs/host").asText())
                        .setPort(Configuration.get("jaxrs/port").asInt())
                        .setProtocol(Configuration.get("jaxrs/protocol").asText())
                        .setPath(jaxrs.get("Path").toString())
                        .build((Object[]) args[0]);
                URI uri =  channel.getUri();
                Entity<?> entity = org.softauto.jaxrs.Utils.buildEntity(produces,(Object[])args[0]);
                logger.debug("invoke PUT for "+ uri + " with headers "+ headers.values() + " entity");
                //return new JerseyHelper(client).put(uri.toString(), methodDescriptor.getProduces(), headers, res,entity);
                return new JerseyHelper(client).put(uri.toString(), produces, headers, res,entity);
            }catch (Exception e){
                logger.error("fail invoke PUT for uri "+ methodDescriptor.getPath()+ " with args "+ Utils.result2String((Object[])args[0]),e);
            }
            return null;
        }
    }

    private static class DELETEMethodHandler implements ServiceCaller.UnaryClass  {

        @Override
        public <T> T invoke(MethodDescriptor methodDescriptor,Object[] args,Map<String, Object> msg,Class<T> res) {
            try {
                //Client client = new JerseyClientFactory().getClient();
                HashMap<String,Object> callOptions = (HashMap<String, Object>) args[2];
                Client client = null;
                if(callOptions.get("feature") != null) {
                    client = ClientBuilder.newBuilder().register((HttpAuthenticationFeature) callOptions.get("feature")).build();
                }else {
                    client = ClientBuilder.newBuilder().build();
                }
                org.softauto.jaxrs.Utils.addProperties((HashMap<String, Object>) callOptions.get("properties"),client);
                //Class[] types = (Class[])args[1];
                //Object[] arguments = (Object[])args[0];
                MultivaluedMap<String, Object> headers = (MultivaluedMap<String, Object>)callOptions.get(Options.headers.name());
                //Entity<?> entity = (Entity<?>)callOptions.get(Options.entity.name());
                String produces = callOptions.get(Options.produce.name()).toString();
                //Class<T> response = (Class)callOptions.get(Options.response.name());
                //MultivaluedMap<String, Object> headers = HeaderBuilder.newBuilder().setHeader(Configuration.get("jaxrs/headers")).build().getHeaders();
                //URI uri = channel.setPath(methodDescriptor.getPath()).getUri((Object[])args[0]);
                Map<String, Object> jaxrs = (Map<String, Object>) msg.get("jaxrs");
                ChannelDescriptor channel = ChannelDescriptor.newBuilder()
                        .setHost(Configuration.get("jaxrs/host").asText())
                        .setPort(Configuration.get("jaxrs/port").asInt())
                        .setProtocol(Configuration.get("jaxrs/protocol").asText())
                        .setPath(jaxrs.get("Path").toString())
                        .build((Object[]) args[0]);
                URI uri =  channel.getUri();
                logger.debug("invoke DELETE for "+ uri + " with headers "+ headers.values() );
                return new JerseyHelper(client).delete(uri.toString(), produces, headers, res);
            }catch (Exception e){
                logger.error("fail invoke DELETE for uri "+ methodDescriptor.getPath()+ " with args "+ Utils.result2String((Object[])args[0]),e);
            }
            return null;
        }
    }

}
