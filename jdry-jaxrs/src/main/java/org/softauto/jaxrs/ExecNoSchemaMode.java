package org.softauto.jaxrs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.grpc.ManagedChannel;
import org.softauto.core.CallbackToResponseStreamObserverAdpater;
import org.softauto.core.Configuration;
import org.softauto.jaxrs.service.MethodDefinition;
import org.softauto.jaxrs.service.RestService;
import org.softauto.jaxrs.service.ServiceDefinition;
import org.softauto.jaxrs.service.ServiceDescriptor;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecNoSchemaMode implements Iexec{

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ExecNoSchemaMode.class);
    private ExecutorService executor = Executors.newSingleThreadExecutor();

/*
    private String gethttpMethod(JsonNode node){
        JsonNode n = node.get("annotations");
        if(n.has("javax.ws.rs.POST")){
            return HttpMethod.POST;
        }
        if(n.has("javax.ws.rs.GET")){
            return HttpMethod.GET;
        }
        if(n.has("javax.ws.rs.PUT")){
            return HttpMethod.PUT;
        }
        if(n.has("javax.ws.rs.DELETE")){
            return HttpMethod.DELETE;
        }
        return null;
    }

 */




    /*
    private String getPath(JsonNode node){
        try {
            JsonNode n = node.get("annotations");
            return  n.get("javax.ws.rs.Path").get("value").asText();

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


     */


    /*
    private javax.ws.rs.core.MediaType getProduce(JsonNode node){
        try {
            JsonNode n = node.get("annotations");
            String value = ((ArrayNode) n.get("javax.ws.rs.Produces").get("value")).get(0).asText();
            return javax.ws.rs.core.MediaType.valueOf(value);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


     */
    /*
    public CallOptions callOptionsConverter(HashMap<String,Object> props){
        CallOptions.Builder builder = CallOptions.newBuilder();
        try {
            String str = new ObjectMapper().writeValueAsString(props);
            JsonNode node = new ObjectMapper().readTree(str);
            builder.setResponse(gethttpMethod(node));
            builder.setProduce(getProduce(node));
            builder.setPath(getPath(node));
        }catch (Exception e){
            e.printStackTrace();
        }
        return builder.build();
    }

     */

    @Override
    public <RespT> void exec(String methodName, org.softauto.serializer.CallFuture<RespT> callback, ManagedChannel channel, Object...args) {
        try {
            executor.submit(()->{
                CallbackToResponseStreamObserverAdpater observerAdpater = new CallbackToResponseStreamObserverAdpater(callback, null);
                HashMap<String,Object> props = (HashMap<String, Object>) args[2];
                //HashMap<String,Object> callOptions = callOptionsConverter(props).getOptions();
                Map<String, Object> msg = (Map<String, Object>) Configuration.get("jaxrs");
                Class<?> returnType = (Class<?>) args[3];
                //args[2] = callOptions;
                ServiceDefinition serviceDefinition = RestService.createServiceDefinition(methodName,Utils.gethttpMethod(props),msg,(Class[])args[1]);
                MethodDefinition md = serviceDefinition.getMethod(org.softauto.core.Utils.extractFullMethodName(methodName));
                RespT res = (RespT)md.getCallerHandler().startCall(md.getMethodDescriptor(),args,md.getMsg(),returnType);
                if (res != null) {
                    observerAdpater.onCompleted((RespT)res);
                } else {
                    observerAdpater.onError(new RuntimeException("Stream got cancelled"));
                }


                logger.debug("successfully exec jaxrs call  "+  methodName);

            });
        }catch (Exception e){
            logger.error("exec jaxrs call  fail "+  methodName,e);
        }
    }
}
