package org.softauto.jaxrs;

import com.fasterxml.jackson.databind.JsonNode;
import io.grpc.ManagedChannel;
import org.softauto.core.*;
import org.softauto.jaxrs.schema.MessageHandler;
import org.softauto.jaxrs.service.MethodDefinition;
import org.softauto.jaxrs.service.RestService;
import org.softauto.jaxrs.service.ServiceDefinition;
import org.softauto.plugin.api.Provider;

import javax.lang.model.element.Element;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * jaxrs plugin Provider Impl
 */
public class JaxrsProviderImpl implements Provider {




    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(JaxrsProviderImpl.class);
    private static JaxrsProviderImpl jaxrsProviderImpl = null;
    Class iface;
    ServiceDefinition serviceDefinition;
    String type = "JAXRS";

    private ExecutorService executor = Executors.newSingleThreadExecutor();


    @Override
    public JaxrsProviderImpl iface(Class iface) {
        this.iface = iface;
        return this;
    }


    public static JaxrsProviderImpl getInstance(){
        if(jaxrsProviderImpl == null){
            jaxrsProviderImpl =  new JaxrsProviderImpl();
        }
        return jaxrsProviderImpl;
    }


    @Override
    public JsonNode parser(Element element) {
        return  new MessageHandler().parser(element);
    }

    @Override
    public <RespT> void exec(String methodName, org.softauto.serializer.CallFuture<RespT> callback, ManagedChannel channel,Object...args) {
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



    @Override
    public void shutdown() {

    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Provider initialize() throws IOException {
        serviceDefinition =  RestService.createServiceDefinition(org.softauto.core.Utils.getRemoteOrLocalClass(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText() ,Configuration.get(Context.STEP_SERVICE_NAME).asText(),Configuration.get(Context.TEST_MACHINE).asText()));
        logger.debug("jaxrs plugin initialize successfully");
        return this;
    }


    @Override
    public void register() {
        ServiceLocator.getInstance().register(type,this);
    }





}
