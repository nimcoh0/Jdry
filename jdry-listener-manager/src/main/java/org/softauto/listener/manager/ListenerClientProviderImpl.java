package org.softauto.listener.manager;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.tools.attach.VirtualMachine;
import io.grpc.ManagedChannel;
import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.core.ServiceLocator;
import org.softauto.core.Utils;
import org.softauto.listener.impl.*;
import org.softauto.plugin.api.Provider;
import org.softauto.serializer.CallFuture;

import javax.lang.model.element.Element;
import java.io.IOException;
import java.lang.management.ManagementFactory;

public class ListenerClientProviderImpl implements Provider {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ListenerClientProviderImpl.class);
    private static ListenerClientProviderImpl listenerClientProviderImpl = null;


    /**
     * this provider name
     */
    String type = "LISTENER-MANAGER";

    /**
     * the schema interface class
     */



    public static ListenerClientProviderImpl getInstance(){
        if(listenerClientProviderImpl == null){
            listenerClientProviderImpl =  new ListenerClientProviderImpl();
        }
        return listenerClientProviderImpl;
    }

    @Override
    public Provider initialize() throws IOException {
        try {
            Class iface = Utils.getRemoteOrLocalClass(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText(), Configuration.get(Context.LISTENER_SERVICE_NAME).asText(), Configuration.get(Context.TEST_MACHINE).asText());
            ListenerServiceImpl listenerServiceImpl = new ListenerServiceImpl();
            Listener.addSchema(iface);
            Listener.init(listenerServiceImpl);
            logger.info("successfully load listener manager");
        }catch (Exception e){
            logger.error("fail to load listener manager",e);
        }
        return this;
    }


    @Override
    public void register() {
        ServiceLocator.getInstance().register("LISTENER-MANAGER",this);
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

        return this;
    }

    @Override
    public <RespT> void exec(String methodName, CallFuture<RespT> callback, ManagedChannel channel, Object... args) {

    }



}
