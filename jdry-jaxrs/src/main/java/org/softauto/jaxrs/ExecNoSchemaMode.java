package org.softauto.jaxrs;

import io.grpc.ManagedChannel;
import org.softauto.core.CallbackToResponseStreamObserverAdpater;
import org.softauto.core.Configuration;
import org.softauto.jaxrs.service.MethodDefinition;
import org.softauto.jaxrs.service.RestService;
import org.softauto.jaxrs.service.ServiceDefinition;
import org.softauto.jaxrs.service.ServiceDescriptor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecNoSchemaMode implements Iexec{

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ExecSchemaMode.class);
    private ExecutorService executor = Executors.newSingleThreadExecutor();


    private String gethttpMethod(HashMap<String,Object> callOptions){
        String annotations = callOptions.get("annotations").toString();
        if(annotations.contains("javax.ws.rs.POST")){
            return "POST";
        }
        if(annotations.contains("javax.ws.rs.GET")){
            return "GET";
        }
        if(annotations.contains("javax.ws.rs.PUT")){
            return "PUT";
        }
        if(annotations.contains("javax.ws.rs.DELETE")){
            return "DELETE";
        }
        return null;
    }

    @Override
    public <RespT> void exec(String methodName, org.softauto.serializer.CallFuture<RespT> callback, ManagedChannel channel, Object...args) {
        try {
            executor.submit(()->{
                CallbackToResponseStreamObserverAdpater observerAdpater = new CallbackToResponseStreamObserverAdpater(callback, null);
                HashMap<String,Object> callOptions = (HashMap<String, Object>) args[2];
                Map<String, Object> msg = (Map<String, Object>) Configuration.get("jaxrs");
                Class<?> returnType = (Class<?>) args[3];
                ServiceDefinition serviceDefinition = RestService.createServiceDefinition(methodName,gethttpMethod(callOptions).toString(),msg,(Class[])args[1]);
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
