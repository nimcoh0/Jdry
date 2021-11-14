package org.softauto.jaxrs.service;


import org.apache.avro.Protocol;
import org.softauto.core.Utils;
import org.softauto.jaxrs.JerseyHelper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedMap;
import java.lang.reflect.Method;
import java.net.URI;
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
            serviceDefinitionBuilder = ServiceDefinition
                    .builder(serviceDescriptor);

            ChannelDescriptor channel = new ChannelDescriptor().build();
            for (Method method : iface.getMethods()) {
                Protocol.Message message = messages.get(method.getName());
                if (message.getProp("transceiver").equals("JAXRS")) {
                    if (message != null) {
                        Map<String, Object> msg = message.getObjectProps();
                        Map<String, Object> jaxrs = (Map<String, Object>) msg.get("jaxrs");
                        String httpMethod = jaxrs.get("HttpMethod").toString();
                        if (httpMethod.equals("GET")) {
                            serviceDefinitionBuilder.addMethod(serviceDescriptor.getMethods(method, message, MethodDescriptor.MethodType.GET),
                                    ServiceCaller.call(new GETMethodHandler()), channel);
                        }
                        if (httpMethod.equals("PUT")) {
                            serviceDefinitionBuilder.addMethod(serviceDescriptor.getMethods(method, message, MethodDescriptor.MethodType.PUT),
                                    ServiceCaller.call(new PUTMethodHandler()), channel);
                        }
                        if (httpMethod.equals("POST")) {
                            serviceDefinitionBuilder.addMethod(serviceDescriptor.getMethods(method, message, MethodDescriptor.MethodType.POST),
                                    ServiceCaller.call(new POSTMethodHandler()), channel);
                        }
                        if (httpMethod.equals("DELETE")) {
                            serviceDefinitionBuilder.addMethod(serviceDescriptor.getMethods(method, message, MethodDescriptor.MethodType.DELETE),
                                    ServiceCaller.call(new DELETEMethodHandler()), channel);
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
        public <T> T invoke(MethodDescriptor methodDescriptor,Object[] args,ChannelDescriptor channel,Class<T> res) {
            try {
                Client client = channel.getClient();
                MultivaluedMap<String, Object> headers = channel.getHeaders();
                URI uri = channel.setPath(methodDescriptor.getPath()).getUri((Object[])args[0]);
                logger.debug("invoke GET for "+ uri);
                return new JerseyHelper(client).get(uri.toString(), methodDescriptor.getProduces(), headers, res);
            }catch (Exception e){
                logger.error("fail invoke GET for uri "+ methodDescriptor.getPath()+ " with args "+ Utils.result2String((Object[])args[0]),e);
            }
            return null;
        }
    }

    private static class POSTMethodHandler implements ServiceCaller.UnaryClass  {

        @Override
        public <T> T invoke(MethodDescriptor methodDescriptor,Object[] args,ChannelDescriptor channel,Class<T> res) {
            try {
                Client client = channel.getClient();
                MultivaluedMap<String, Object> headers = channel.getHeaders();
                URI uri = channel.setPath(methodDescriptor.getPath()).getUri((Object[])args[0]);
                Entity<?> entity = methodDescriptor.buildEntity(methodDescriptor.getMessage(),(Object[])args[0]);
                logger.debug("invoke POST for "+ uri + " with headers "+ headers.values() + " entity");
                return new JerseyHelper(client).post(uri.toString(), methodDescriptor.getProduces(), headers, res,entity);

            }catch (Exception e){
                logger.error("fail invoke POST for uri "+ methodDescriptor.getPath()+ " with args "+ Utils.result2String((Object[])args[0]),e);
            }
            return null;
        }
    }

    private static class PUTMethodHandler implements ServiceCaller.UnaryClass  {

        @Override
        public <T> T invoke(MethodDescriptor methodDescriptor,Object[] args,ChannelDescriptor channel,Class<T> res) {
            try {
                Client client = channel.getClient();
                MultivaluedMap<String, Object> headers = channel.getHeaders();
                URI uri = channel.setPath(methodDescriptor.getPath()).getUri((Object[])args[0]);
                Entity<?> entity = methodDescriptor.buildEntity(methodDescriptor.getMessage(),(Object[])args[0]);
                logger.debug("invoke PUT for "+ uri + " with headers "+ headers.values() + " entity");
                return new JerseyHelper(client).put(uri.toString(), methodDescriptor.getProduces(), headers, res,entity);
            }catch (Exception e){
                logger.error("fail invoke PUT for uri "+ methodDescriptor.getPath()+ " with args "+ Utils.result2String((Object[])args[0]),e);
            }
            return null;
        }
    }

    private static class DELETEMethodHandler implements ServiceCaller.UnaryClass  {

        @Override
        public <T> T invoke(MethodDescriptor methodDescriptor,Object[] args,ChannelDescriptor channel,Class<T> res) {
            try {
                Client client = channel.getClient();
                MultivaluedMap<String, Object> headers = channel.getHeaders();
                URI uri = channel.setPath(methodDescriptor.getPath()).getUri((Object[])args[0]);
                logger.debug("invoke DELETE for "+ uri + " with headers "+ headers.values() );
                return new JerseyHelper(client).delete(uri.toString(), methodDescriptor.getProduces(), headers, res);
            }catch (Exception e){
                logger.error("fail invoke DELETE for uri "+ methodDescriptor.getPath()+ " with args "+ Utils.result2String((Object[])args[0]),e);
            }
            return null;
        }
    }

}
