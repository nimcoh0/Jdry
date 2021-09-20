package org.softauto.local;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonArray;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import org.apache.avro.Protocol;
import org.apache.avro.Schema;
import org.softauto.core.*;
import org.softauto.local.schema.MessageHandler;
import org.softauto.plugin.api.Provider;
import javax.lang.model.element.Element;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class LocalProviderImpl implements Provider {


    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(LocalProviderImpl.class);
    private static LocalProviderImpl localProviderImpl = null;
    Class iface;
    String type = "LOCAL";
    Method method ;
    String fullClassName;
    Protocol.Message msg;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public static LocalProviderImpl getInstance(){
        if(localProviderImpl == null){
            localProviderImpl =  new LocalProviderImpl();
        }
        return localProviderImpl;
    }


    @Override
    public Provider initilize() throws IOException {
       return this;
    }

    @Override
    public void register() {
        ServiceLocator.getInstance().register(type,this);
    }

    @Override
    public void shutdown() {

    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public JsonNode parser(Element element) {
        return  new MessageHandler().parser(element);
    }

    @Override
    public Provider iface(Class iface) {
        this.iface = iface;
        return this;
    }

    @Override
    public <RespT> void exec(String methodName, Object[] args, CallFuture<RespT> callback, ManagedChannel channel) {

            executor.submit(()->{
                try {
                   // CallbackToResponseStreamObserverAdpater observerAdpater = new CallbackToResponseStreamObserverAdpater(callback, null);
                    Protocol protocol = Utils.getProtocol(iface);
                    msg = protocol.getMessages().get(methodName);
                    method = Utils.getMethodByNameAndTypeNames(methodName, iface, msg);
                    fullClassName = ((HashMap)msg.getObjectProp("class")).get("fullClassName").toString();
                    new LocalClient().setFullClassName(fullClassName).setMethod(method).setMsg(msg).setArgs(args).call();
                    logger.debug("successfully exec Local call  " + methodName);
                }catch (Exception e){
                    e.printStackTrace();
                }
            });

    }



}
