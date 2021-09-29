package org.softauto.local;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonArray;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import org.apache.avro.Protocol;
import org.apache.avro.Schema;
import org.softauto.core.*;
import org.softauto.local.schema.MessageHandler;
import org.softauto.plugin.api.Provider;
import javax.lang.model.element.Element;
import javax.rmi.CORBA.Util;
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
    Injector injector = Guice.createInjector();

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
    public <RespT> void exec(String methodName, org.softauto.serializer.CallFuture<RespT> callback, ManagedChannel channel,Object...args) {

            executor.submit(()->{
                try {
                   // CallbackToResponseStreamObserverAdpater observerAdpater = new CallbackToResponseStreamObserverAdpater(callback, null);
                    //Protocol protocol = Utils.getProtocol(iface);
                    //msg = protocol.getMessages().get(methodName);
                    fullClassName = Utils.getFullClassName(methodName);
                    Object o = injector.getInstance(Utils.findClass(fullClassName));
                    //method = Utils.getMethodByNameAndTypeNames(methodName, iface, msg);
                    method = Utils.getMethod(o,methodName, (Class[]) args[1]);

                    new LocalClient().setServiceImpl(o).setMethod(method).setArgs((Object[]) args[0]).call(callback);
                    logger.debug("successfully exec Local call  " + methodName);
                }catch (Exception e){
                    e.printStackTrace();
                }
            });

    }



}
