package org.softauto.listener.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.core.ServiceLocator;
import org.softauto.core.Utils;
import org.softauto.plugin.api.Provider;
import org.softauto.serializer.CallFuture;
import org.softauto.serializer.service.SerializerService;

import javax.lang.model.element.Element;
import java.io.File;


/**
 * listener server impl class.
 *
 */
public class ListenerServerProviderImpl implements Provider {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ListenerServerProviderImpl.class);
    private static ListenerServerProviderImpl listenerProviderImpl = null;
    ObjectMapper objectMapper;


    /**
     * this provider name
     */
    String type = "LISTENER-SERVER";

    /**
     * the schema interface class
     */
    Class iface;

    /**
     * listener server
     */
    Server server = null;




    private ListenerServerProviderImpl(){
        objectMapper = new ObjectMapper(new YAMLFactory());
    }


    public static ListenerServerProviderImpl getInstance(){
        if(listenerProviderImpl == null){
            listenerProviderImpl =  new ListenerServerProviderImpl();
         }
        return listenerProviderImpl;
    }







    public ListenerServerProviderImpl initialize()  {
        try {
            server = ServerBuilder.forPort(Configuration.get(Context.LISTENER_PORT).asInt())
                    .addService(org.softauto.serializer.SoftautoGrpcServer.createServiceDefinition(SerializerService.class, ListenerServiceImpl.class))
                    .build();
            server.start();
            logger.info("listener server load successfully on port "+ Configuration.get(Context.LISTENER_PORT).asInt());
        }catch (Exception e) {
            logger.error("start Listener server fail  ", e);
        }

        return this;
    }



    public void register() {
        ServiceLocator.getInstance().register("LISTENER-SERVER",server);

    }

    @Override
    public void shutdown(){
       server.shutdown();
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
    public ListenerServerProviderImpl iface(Class iface) {
        this.iface = iface;
        return this;
    }

    @Override
    public <RespT> void exec(String methodName, CallFuture<RespT> callback, ManagedChannel channel, Object... args) {

    }



}
