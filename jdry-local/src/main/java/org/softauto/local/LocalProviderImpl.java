package org.softauto.local;

import com.fasterxml.jackson.databind.JsonNode;
import io.grpc.ManagedChannel;
import org.softauto.core.*;
import org.softauto.local.schema.MessageHandler;
import org.softauto.plugin.api.Provider;
import javax.lang.model.element.Element;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class LocalProviderImpl implements Provider {


    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(LocalProviderImpl.class);
    private static LocalProviderImpl localProviderImpl = null;
    Class iface;
    String type = "LOCAL";
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public static LocalProviderImpl getInstance(){
        if(localProviderImpl == null){
            localProviderImpl =  new LocalProviderImpl();
        }
        return localProviderImpl;
    }


    @Override
    public Provider initialize() throws IOException {
       logger.info("local plugin initialize successfully");
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
                    Object methodResponse = null;
                    String fullClassName = Utils.getFullClassName(methodName);
                    Object o = Utils.findClass(fullClassName).newInstance();
                    Method method = Utils.getMethod(o,methodName, (Class[]) args[1]);
                    method.setAccessible(true);
                    logger.debug("invoking "+ methodName+ " with " + Arrays.toString(args));
                    if (Modifier.isStatic(method.getModifiers())) {
                        methodResponse = method.invoke(null, (Object[])args[0]);
                    } else {
                        methodResponse = method.invoke(o, (Object[])args[0]);
                    }
                    logger.debug("got result "+methodResponse);
                    callback.handleResult((RespT) methodResponse);
                    //new LocalClient().setServiceImpl(o).setMethod(method).setArgs((Object[]) args[0]).call(callback);
                    logger.debug("successfully exec Local call  " + methodName);
                }catch (Exception e){
                    logger.error("fail execute local call "+ methodName,e);
                    callback.handleError(e);
                }
            });

    }




}
