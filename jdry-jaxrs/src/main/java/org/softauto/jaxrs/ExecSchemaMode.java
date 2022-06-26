package org.softauto.jaxrs;

import io.grpc.ManagedChannel;
import org.softauto.core.CallbackToResponseStreamObserverAdpater;
import org.softauto.jaxrs.service.MethodDefinition;
import org.softauto.jaxrs.service.ServiceDefinition;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecSchemaMode implements Iexec{

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ExecSchemaMode.class);
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    ServiceDefinition serviceDefinition;

    public void setServiceDefinition(ServiceDefinition serviceDefinition) {
        this.serviceDefinition = serviceDefinition;
    }

    @Override
    public <RespT> void exec(String methodName, org.softauto.serializer.CallFuture<RespT> callback, ManagedChannel channel, Object...args) {
        try {
            executor.submit(()->{
                CallbackToResponseStreamObserverAdpater observerAdpater = new CallbackToResponseStreamObserverAdpater(callback, null);
                MethodDefinition md = serviceDefinition.getMethod(org.softauto.core.Utils.extractFullMethodName(methodName));
                RespT res = (RespT)md.getCallerHandler().startCall(md.getMethodDescriptor(),args,md.getMsg(),md.getMethodDescriptor().getMethod().getReturnType());
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
