package org.softauto.listener.server;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.core.TestLifeCycle;
import org.softauto.core.Utils;
import org.softauto.serializer.service.Message;
import org.softauto.serializer.service.SerializerService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ListenerServiceImpl implements SerializerService{

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ListenerServiceImpl.class);
    private Class listener = null;

    public ListenerServiceImpl(){
        this.listener =  Utils.getRemoteOrLocalClass(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH) , "Listener", Configuration.get(Context.TEST_MACHINE));
    }


    @Override
    public synchronized Object execute(Message message) throws Exception {
        Object methodResponse = null;
        try {
            if(message.getDescriptor().equals("log") || message.getDescriptor().equals("logError")){
                printLog(message);
                return null;
            }
            if (Context.getTestState().equals(TestLifeCycle.START)) {

                logger.debug("execute message " + message.toJson());
                String fullClassName = Configuration.get(Context.LISTENER_SERVICE_IMPL_FOR_PROXY);
                //String fullServiceClassName = message.getService();
                Object o = ListenerObserver.getInstance().getLastChannel(fullClassName);
                Method method = Utils.getMethod2(o, message.getDescriptor(), message.getTypes());
                if (method == null) {
                    logger.warn("no method found for " + message.getDescriptor() + " types " + Utils.result2String(message.getTypes()) + " on " + o.getClass().getName());
                    return message.getArgs();
                    //throw new Exception("method not found for " + message.getDescriptor());
                }
                logger.debug("invoking " + message.getDescriptor() + " args " + Utils.result2String(message.getArgs()));
                method.setAccessible(true);
                if (Modifier.isStatic(method.getModifiers())) {
                    methodResponse = method.invoke(null, message.getArgs());
                } else {
                    methodResponse = method.invoke(o, message.getArgs());
                }

            }else {
                return new Object[]{message.getArgs()};
            }
            } catch(InvocationTargetException e){
                logger.error("fail invoke method " + message.getDescriptor(), e);

            } catch(Exception e){
                logger.error("fail invoke method " + message.getDescriptor(), e);

            }

        return methodResponse;

    }

    private void printLog(Message message){
        Marker marker = MarkerManager.getMarker(message.getData("marker").toString());
        if(marker == null){
            marker = MarkerManager.getMarker("SUT");
        }
        if(message.getData("ex") != null){
            logger.log(Level.getLevel(message.getData("level").toString()), marker, message.getData("log").toString(), new Exception(message.getData("ex").toString()));
        }else {
            //logger.log(Level.getLevel(message.getData("level").toString()), marker, message.getData("log").toString(), message.getData("clazz"));
            //logger.log(Level.getLevel(message.getData("level").toString()),  message.getData("log").toString());
            org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Utils.findClass(message.getData("clazz").toString()));
            logger.debug(marker,message.getData("log").toString(), message.getData("clazz"));
        }
    }

    /*
    @Override
    public void execute(Message message, CallFuture<Object> callback) throws Exception {
        Object methodResponse = null;
        injector = ListenerServerProviderImpl.getInstance().getInjector();
        try {
            String fullClassName = message.getService();
            Class iface = Utils.findClass(fullClassName);
            Object serviceImpl = injector.getInstance(iface);
            Method method = Utils.getMethod2(serviceImpl, message.getDescriptor(), message.getTypes());
            Object channel = ListenerObserver.getInstance().getLastChannel(serviceImpl.getClass().getName());
            if(channel == null){
                channel = serviceImpl;
            }
            logger.debug("invoking " + message.getDescriptor());
            method.setAccessible(true);
            if (Modifier.isStatic(method.getModifiers())) {
                methodResponse = method.invoke(null, message.getArgs());
            } else {
                methodResponse = method.invoke(channel, message.getArgs());
            }


        } catch (InvocationTargetException e) {
            logger.error("fail invoke method "+ message.getDescriptor(),e );
            callback.handleError(e);

        } catch (Exception e) {
            logger.error("fail invoke method "+ message.getDescriptor(),e );
            callback.handleError(e);
        }
        callback.handleResult(methodResponse);

    }


    @Override
    public Object execute(Message message) throws Exception {
        Object methodResponse = null;
        injector = ListenerServerProviderImpl.getInstance().getInjector();
        try {
            String fullClassName = message.getService();
            Class iface = Utils.findClass(fullClassName);
            Object serviceImpl = injector.getInstance(iface);
            Method method = Utils.getMethod2(serviceImpl, message.getDescriptor(), message.getTypes());
            Object channel = ListenerObserver.getInstance().getLastChannel(serviceImpl.getClass().getName());
            if(channel == null){
                channel = serviceImpl;
            }
            logger.debug("invoking " + message.getDescriptor());
            method.setAccessible(true);
            if (Modifier.isStatic(method.getModifiers())) {
                methodResponse = method.invoke(null, message.getArgs());
            } else {
                methodResponse = method.invoke(channel, message.getArgs());
            }


        } catch (InvocationTargetException e) {
            logger.error("fail invoke method "+ message.getDescriptor(),e );

        } catch (Exception e) {
            logger.error("fail invoke method "+ message.getDescriptor(),e );

        }
        return methodResponse;

    }

     */
}
