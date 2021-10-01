package org.softauto.local;

import org.apache.avro.Protocol;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class LocalClient {

    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(LocalClient.class);
    String fullClassName;
    Protocol.Message msg;
    Method method;
    Object[] args;
    Object serviceImpl;


    public LocalClient setServiceImpl(Object serviceImpl) {
        this.serviceImpl = serviceImpl;
        return this;
    }

    public LocalClient setFullClassName(String fullClassName) {
        this.fullClassName = fullClassName;
        return this;
    }

    public LocalClient setMsg(Protocol.Message msg) {
        this.msg = msg;
        return this;
    }

    public LocalClient setMethod(Method method) {
        this.method = method;
        return this;
    }

    public LocalClient setArgs(Object[] args) {
        this.args = args;
        return this;
    }

    public <RespT> void call(org.softauto.serializer.CallFuture<RespT> callback) throws Exception {
        Object methodResponse = null;
        try {
            logger.debug("invoking " + method.getName());
            method.setAccessible(true);
            if (Modifier.isStatic(method.getModifiers())) {
                methodResponse = method.invoke(null, args);
            } else {
                methodResponse = method.invoke(serviceImpl, args);
            }


        } catch (
                InvocationTargetException e) {
            logger.error("fail invoke method "+ method.getName(),e );
            callback.handleError(e);
        } catch (Exception e) {
            logger.error("fail invoke method "+ method.getName(),e );
            callback.handleError(e);
        }
        callback.handleResult((RespT) methodResponse);
    }

    /*
    public Object call() throws Exception {
       if ((method.getParameterTypes().length > 0) && (method.getParameterTypes()[method.getParameterTypes().length - 1] instanceof Class)
              && org.softauto.serializer.CallFuture.class.isAssignableFrom(((Class<?>) method.getParameterTypes()[method.getParameterTypes().length - 1]))) {
             // get the callback argument from the end
             logger.debug("invoke async request :" + method);
             Object[] finalArgs = Arrays.copyOf(args, args.length - 1);
             org.softauto.serializer.CallFuture<?> callback = (org.softauto.serializer.CallFuture<?>) args[args.length - 1];
             unaryRequest(method, finalArgs, callback);
             return null;
        } else {
            logger.debug("invoke sync request :"+ method);
            return unaryRequest(method, args);

        }
    }

    private Object unaryRequest(Method method, Object[] args) throws Exception {
        org.softauto.serializer.CallFuture<?> callFuture = new org.softauto.serializer.CallFuture<>();
        unaryRequest(method, args, callFuture);
        return callFuture.get();
    }


    private <RespT> void unaryRequest(Method method, Object[] args, org.softauto.serializer.CallFuture<RespT> callback) throws Exception {
       // Provider provider = ProviderManager.provider(transceiver).create();
        logger.debug("invoke method " + method.getName());
        //provider.exec( methodName,  args,  callback,null);
        StreamObserver<Object> observerAdpater = new CallbackToResponseStreamObserverAdpater(callback,null);
        invoke(args, observerAdpater);
    }

    private void invoke(Object[] request, StreamObserver<Object> responseObserver) {
        Object methodResponse = null;
        try {
            //Object serviceImpl = Utils.getClassInstance(fullClassName, msg, request, method);
            //if(!msg.hasProp("type") || !msg.getProp("type").equals("constructor")) {
                //Method m = Utils.getMethod(serviceImpl, method.getName(), method.getParameterTypes());
                logger.debug("invoking " + method);
            method.setAccessible(true);
                if (Modifier.isStatic(method.getModifiers())) {
                    methodResponse = method.invoke(null, request);
                } else {
                    methodResponse = method.invoke(serviceImpl, request);
                }
            //}else {
              //  methodResponse = serviceImpl;
            //}

        } catch (InvocationTargetException e) {
            logger.error("fail invoke method "+ method,e );
            methodResponse = e.getTargetException();
        } catch (Exception e) {
            logger.error("fail invoke method "+ method,e );
            methodResponse = e;
        }
        responseObserver.onNext(methodResponse);
        responseObserver.onCompleted();
    }

*/

}



