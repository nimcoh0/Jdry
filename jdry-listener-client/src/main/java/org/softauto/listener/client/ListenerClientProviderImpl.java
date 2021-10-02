package org.softauto.listener.client;

import com.fasterxml.jackson.databind.JsonNode;
import io.grpc.ManagedChannel;
import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.core.ServiceLocator;
import org.softauto.core.Utils;
import org.softauto.listener.Listener;
import org.softauto.plugin.api.Provider;
import org.softauto.serializer.CallFuture;

import javax.lang.model.element.Element;
import java.io.IOException;

public class ListenerClientProviderImpl implements Provider {

    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(ListenerClientProviderImpl.class);
    private static ListenerClientProviderImpl listenerClientProviderImpl = null;
    Listener listener;

    /**
     * this provider name
     */
    String type = "LISTENER-CLIENT";

    /**
     * the schema interface class
     */
    Class iface;


    public static ListenerClientProviderImpl getInstance(){
        if(listenerClientProviderImpl == null){
            listenerClientProviderImpl =  new ListenerClientProviderImpl();
        }
        return listenerClientProviderImpl;
    }


    @Override
    public Provider initilize() throws IOException {
        listener = Listener.newlistenerFactory().setAspectjweaver(Configuration.get(Context.ASPECT_WEAVER).asText()).setServiceImpl(new ListenerServiceImpl()).getListener();
        iface = Utils.getRemoteOrLocalClass(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText() , Context.LISTENER_SERVICE,Configuration.get(Context.TEST_MACHINE).asText());
        return this;
    }

    public  Class getIface() {
        return iface;
    }


    @Override
    public void register() {
        ServiceLocator.getInstance().register("LISTENER-CLIENT",listener);
    }

    @Override
    public void shutdown() {
        listener.shutdown();
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
