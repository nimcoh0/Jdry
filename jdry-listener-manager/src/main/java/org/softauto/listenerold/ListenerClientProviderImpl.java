package org.softauto.listenerold;

import com.fasterxml.jackson.databind.JsonNode;
import io.grpc.ManagedChannel;
import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.core.ServiceLocator;
import org.softauto.core.Utils;
import org.softauto.listener.impl.Listener;
import org.softauto.plugin.api.Provider;
import org.softauto.serializer.CallFuture;
import javax.lang.model.element.Element;
import java.io.IOException;


public class ListenerClientProviderImpl implements Provider {

    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(ListenerClientProviderImpl.class);
    private static ListenerClientProviderImpl listenerClientProviderImpl = null;


    /**
     * this provider name
     */
    String type = "LISTENER-CLIENT";





    public static ListenerClientProviderImpl getInstance(){
        if(listenerClientProviderImpl == null){
            listenerClientProviderImpl =  new ListenerClientProviderImpl();
        }
        return listenerClientProviderImpl;
    }


    @Override
    public Provider initilize() throws IOException {
        Class iface = Utils.getRemoteOrLocalClass(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText() , Context.LISTENER_SERVICE,Configuration.get(Context.TEST_MACHINE).asText());
        org.softauto.listenerold.ListenerServiceImpl listenerServiceImpl = new org.softauto.listenerold.ListenerServiceImpl();
        Listener.addSchema(iface);
        Listener.init(listenerServiceImpl);
        return this;
    }


    @Override
    public void register() {
        ServiceLocator.getInstance().register("LISTENER-CLIENT",this);
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
