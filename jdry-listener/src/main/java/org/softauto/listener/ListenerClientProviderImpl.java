package org.softauto.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.tools.attach.VirtualMachine;
import io.grpc.ManagedChannel;
import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.core.ServiceLocator;
import org.softauto.core.Utils;
import org.softauto.plugin.api.Provider;
import org.softauto.serializer.CallFuture;

import javax.lang.model.element.Element;
import java.io.IOException;
import java.lang.management.ManagementFactory;

public class ListenerClientProviderImpl implements Provider {

    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(ListenerClientProviderImpl.class);
    private static ListenerClientProviderImpl listenerClientProviderImpl = null;
    //Listener listener;

    /**
     * this provider name
     */
    String type = "LISTENER-CLIENT";

    /**
     * the schema interface class
     */
    Class iface;
    Class ifaceLog;

    Object serviceImpl;


    public static ListenerClientProviderImpl getInstance(){
        if(listenerClientProviderImpl == null){
            listenerClientProviderImpl =  new ListenerClientProviderImpl();
        }
        return listenerClientProviderImpl;
    }

    public  Object getServiceImpl() {
        return new ListenerServiceImpl();
    }

    @Override
    public Provider initilize() throws IOException {
        //listener = Listener.newlistenerFactory().setAspectjweaver(Configuration.get(Context.ASPECT_WEAVER).asText()).setServiceImpl(new ListenerServiceImpl()).getListener();
        iface = Utils.getRemoteOrLocalClass(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText() , Context.LISTENER_SERVICE,Configuration.get(Context.TEST_MACHINE).asText());
        ifaceLog = Utils.getRemoteOrLocalClass(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText() , Context.LISTENER_SERVICE_LOG,Configuration.get(Context.TEST_MACHINE).asText());
        startWeaver(Configuration.get(Context.ASPECT_WEAVER).asText());
        return this;
    }

    public  Class getIface() {
        return iface;
    }


    public Class getServiceClass(String name){
        if(name.equals("tests.infrastructure."+Context.LISTENER_SERVICE)) {
            return iface;
        }
        if(name.equals("tests.infrastructure."+Context.LISTENER_SERVICE_LOG)) {
            return ifaceLog;
        }
        return null;
    }


    public Class getIfaceLog() {
        return ifaceLog;
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
        this.iface = iface;
        return this;
    }

    @Override
    public <RespT> void exec(String methodName, CallFuture<RespT> callback, ManagedChannel channel, Object... args) {

    }

    public  void startWeaver(String aspectjweaver){
        try {
            String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
            VirtualMachine jvm = VirtualMachine.attach(pid);
            jvm.loadAgent(aspectjweaver);
            jvm.detach();
            logger.info("Listener server Load successfully ");
        }catch (Exception e){
            logger.fatal("load Listener fail ",e);
            System.exit(1);
        }

    }
}
