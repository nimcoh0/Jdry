package org.softauto.tester;

import org.softauto.core.Configuration;
import org.softauto.plugin.ProviderManager;
import org.softauto.plugin.api.Provider;
import org.softauto.serializer.CallFuture;
import org.softauto.serializer.Serializer;
import org.softauto.serializer.service.Message;

import java.util.Arrays;

public class InvocationHandler {

    private  org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(InvocationHandler.class);

    public  void invoke(String methodName, Object[] args, Class[] types, CallFuture callback,String transceiver)  {
        try {
            Provider provider = ProviderManager.provider(transceiver).create();
            logger.debug("invoke method " + methodName+ " using protocol "+ transceiver);
            provider.exec( methodName, callback,null,new Object[]{args,types});
            logger.debug("callback value "+callback.getResult()+" get error "+callback.getError());
        } catch (Exception e) {
            logger.error("fail invoke method "+ methodName+ " with args "+ Arrays.toString(args),e);
        }

    }

    public <T> T invoke(String methodName, Object[] args, Class[] types)  throws Exception{
        CallFuture<T> future = new CallFuture<>();
        T t = null;
        try {
            String host = Configuration.get("serializer_host").asText();
            int port = Configuration.get("serializer_port").asInt();
            Serializer serializer = new Serializer().setHost(host).setPort(port).build();
            Message message = Message.newBuilder().setDescriptor(methodName).setArgs(args).setTypes(types).build();
            t = serializer.write(message);
            //t = future.get();
        } catch (Exception e) {
            logger.error("fail invoke method "+ methodName+ " with args "+ Arrays.toString(args),e);
        }
        return t;
    }




}
