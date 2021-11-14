package org.softauto.socket;

import com.fasterxml.jackson.databind.JsonNode;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import org.softauto.core.CallbackToResponseStreamObserverAdpater;
import org.softauto.core.Configuration;
import org.softauto.core.ServiceLocator;
import org.softauto.plugin.api.Provider;
import org.softauto.serializer.CallFuture;
import org.softauto.socket.schema.MessageHandler;

import javax.lang.model.element.Element;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * socket client provider impl class, this class is a singleton .
 * set client env and call socket impl
 *
 */
public class SocketProviderImpl implements Provider {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(SocketProviderImpl.class);


    /**
     * this provider name
     */
    String type = "SOCKET";

    /**
     * the schema interface class
     */
    Class iface;




    private static SocketProviderImpl socketProviderImpl = null;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public SocketProviderImpl iface(Class iface) {
        this.iface = iface;
        return this;
    }




    public static SocketProviderImpl getInstance(){
        if(socketProviderImpl == null){
            socketProviderImpl =  new SocketProviderImpl();
        }
        return socketProviderImpl;
    }



    @Override
    public Provider initialize() throws IOException {
        logger.info("sock plugin initialize");
        return this;
    }



    /**
     * register this plugin provider with in the ServiceLocator
     */
    @Override
    public void register() {
        ServiceLocator.getInstance().register(type,this);
    }

    /**
     * shutdown this plugin
     */
    @Override
    public void shutdown() {
        executor.shutdown();
        logger.info("shutdown socket service");
    }

    @Override
    public String getType() {
        return type;
    }


    /**
     * parse element for the schema
     * @param element
     * @return
     */
    @Override
    public JsonNode parser(Element element) {
        return  new MessageHandler().parser(element);
    }

    /**
     * execute org.softauto.socket request
     * @param methodName
     * @param args
     * @param callback
     * @param <RespT>
     */
    @Override
    public <RespT> void exec(String methodName,  CallFuture<RespT> callback, ManagedChannel channel,Object...args) {
        try{
            executor.submit(()->{
                String host = Configuration.get("socket/host").asText();
                int port = Configuration.get("socket/port").asInt();
                StreamObserver<Object> observerAdpater = new CallbackToResponseStreamObserverAdpater<>(callback, null);
                byte[] b = new ClientCallImpl(host, port).sendMessage(args[0].toString(), (Integer) args[1]);
                if (b.length == (Integer) args[1]) {
                    observerAdpater.onCompleted();
                } else {
                    observerAdpater.onError(new RuntimeException("Stream got cancelled"));
                }
                logger.debug("exec socket request " + methodName  + "with args "+ Arrays.toString(args) + " successfully");
            });
        }catch (Exception e){
            logger.error("exec socket request " + methodName + " fail" + "with args "+ Arrays.toString(args) ,e);
        }
    }


}
