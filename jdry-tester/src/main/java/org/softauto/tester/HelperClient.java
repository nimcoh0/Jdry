package org.softauto.tester;

import org.softauto.core.CallFuture;
import org.softauto.plugin.ProviderManager;
import org.softauto.plugin.api.Provider;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

public class HelperClient {

    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(HelperClient.class);
    String transceiver;
    String method;

    public Object invoke(String protocol,String method, Object[] args, Class[] types)  {
        try {
            this.transceiver = protocol;
            this.method = method;
            if(args == null){
                args = new Object[]{};
            }
            logger.debug("invoke method "+ method+ " with args "+ Arrays.toString(args));
            return  invokeUnaryMethod(method, args,types);
        } catch (Exception e) {
            logger.error("fail invoke method "+ method+ " with args "+ Arrays.toString(args),e);
        }
        return null;
    }


    private Object invokeUnaryMethod(String  method, Object[] args,Class[] parameterTypes) throws Exception {

        if ((parameterTypes.length > 0) && (parameterTypes[parameterTypes.length - 1] instanceof Class)
                && CallFuture.class.isAssignableFrom(((Class<?>) parameterTypes[parameterTypes.length - 1]))) {
            // get the callback argument from the end
            logger.debug("invoke async request :"+ method);
            Object[] finalArgs = Arrays.copyOf(args, args.length - 1);
            CallFuture<?> callback = (CallFuture<?>) args[args.length - 1];
            unaryRequest(method, finalArgs, callback);
            return null;
        } else {
            logger.debug("invoke sync request :"+ method);
            return unaryRequest(method, args);

        }
    }

    private Object unaryRequest(String methodName, Object[] args) throws Exception {
        CallFuture<?> callFuture = new CallFuture<>();
        unaryRequest(methodName, args, callFuture);
        return callFuture.get();
    }


    private <RespT> void unaryRequest(String methodName, Object[] args, CallFuture<RespT> callback) throws Exception {
        Provider provider = ProviderManager.provider(transceiver).create();
        logger.debug("invoke method " + methodName+ " using protocol "+ transceiver);
        provider.exec( methodName,  args,  callback,null);
    }

}



