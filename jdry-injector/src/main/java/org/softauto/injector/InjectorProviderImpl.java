package org.softauto.injector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.ManagedChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.core.ServiceLocator;
import org.softauto.core.Utils;
import org.softauto.plugin.api.Provider;
import org.softauto.serializer.CallFuture;

import javax.lang.model.element.Element;
import java.io.IOException;


/**
 * initilize the injector
 */
public class InjectorProviderImpl implements Provider {

    Injector injector = null;
    private static final Logger logger = LogManager.getLogger(InjectorProviderImpl.class);
    private static InjectorProviderImpl injectorProviderImpl = null;
    public ObjectMapper objectMapper = null;
    String type = "INJECTOR";
    Class iface;

    public static InjectorProviderImpl getInstance(){
        if(injectorProviderImpl == null){
            injectorProviderImpl =  new InjectorProviderImpl();
        }
        return injectorProviderImpl;
    }

   private InjectorProviderImpl(){
       objectMapper = new ObjectMapper();
   }




    public InjectorProviderImpl initialize() throws IOException {
        try {
            this.iface =  Utils.getRemoteOrLocalClass(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText() , Configuration.get(Context.STEP_SERVICE_NAME).asText(), Configuration.get(Context.TEST_MACHINE).asText());
            injector = new Injector().createServiceDefinition(iface);
            logger.info("Injector successfully initilize");
        }catch (Throwable e){
            logger.fatal("fail to load injector ", e);
            System.exit(1);
        }finally {

        }
        return this;
    }


    public void register() {
        ServiceLocator.getInstance().register("INJECTOR",injector);
    }

    @Override
    public void shutdown() {

    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public JsonNode parser(Element element) {
        return null;
    }

    @Override
    public Provider iface(Class iface) {
        this.iface = iface;
        return this;
    }

    @Override
    public <RespT> void exec(String methodName, CallFuture<RespT> callback, ManagedChannel channel, Object... args) {

    }



}
