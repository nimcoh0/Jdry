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
    ExecSchemaMode execSchemaMode = new ExecSchemaMode();
    ExecNoSchemaMode execNoSchemaMode = new ExecNoSchemaMode();


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
        if(Configuration.get("schemaMode")){
            execSchemaMode.setServiceDefinition(serviceDefinition);
            execSchemaMode.exec(methodName,callback,channel,args);
        }else {
            execNoSchemaMode.exec(methodName,callback,channel,args);
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
        try {
            if(Configuration.get("schemaMode")){
                Class listenerService = Class.forName("tests.infrastructure.StepService");
                serviceDefinition = RestService.createServiceDefinition(listenerService);
                logger.debug("jaxrs plugin initialize successfully");
            }else {
                //schemaMode = false;
                logger.debug("StepService not found  jaxrs plugin initialize in no schema mode successfully");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }


    @Override
    public void register() {
        ServiceLocator.getInstance().register(type,this);
    }





}
