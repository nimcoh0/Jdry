package org.softauto.tester.listener;


import com.google.inject.Injector;
import org.softauto.core.Utils;
import org.softauto.system.SystemServiceImpl;
import org.softauto.serializer.CallFuture;
import org.softauto.serializer.service.Message;
import org.softauto.serializer.service.SerializerService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


public class SerializerServiceImpl implements SerializerService,SerializerService.Callback{

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(SerializerServiceImpl.class);
    private Injector injector ;



    public SerializerServiceImpl(){
        try {
            injector = SystemServiceImpl.getInstance().getInjector();
            //injector = Guice.createInjector(new BasicModule());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

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

    /*
    @Override
    public void execute(Message message, CallFuture<Object> callback) throws Exception {
        Object methodResponse = null;
        try {
            String fullClassName = Utils.getFullClassName(message.getDescriptor());
            String methodName = Utils.getMethodName(message.getDescriptor());
            Object serviceImpl = injector.getInstance(Utils.findClass(fullClassName));
            Method m = Utils.getMethod(serviceImpl, methodName, message.getTypes());
            logger.debug("invoking " + message.getDescriptor());
            m.setAccessible(true);
            if (Modifier.isStatic(m.getModifiers())) {
                methodResponse = m.invoke(null, message.getArgs());
            } else {
                methodResponse = m.invoke(serviceImpl, message.getArgs());
            }


        } catch (
            InvocationTargetException e) {
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
        try {
            String fullClassName = Utils.getFullClassName(message.getDescriptor());
            Object serviceImpl = injector.getInstance(Utils.findClass(fullClassName));
            String methodName = Utils.getMethodName(message.getDescriptor());
            Method m = Utils.getMethod(serviceImpl, methodName, message.getTypes());
            logger.debug("invoking " + message.getDescriptor());
            m.setAccessible(true);
            if (Modifier.isStatic(m.getModifiers())) {
                methodResponse = m.invoke(null, message.getArgs());
            } else {
                methodResponse = m.invoke(serviceImpl, message.getArgs());
            }


        } catch (
                InvocationTargetException e) {
            logger.error("fail invoke method "+ message.getDescriptor(),e );
            methodResponse = e.getTargetException();

        } catch (Exception e) {
            logger.error("fail invoke method "+ message.getDescriptor(),e );
            methodResponse = e;
        }
        return methodResponse;
    }
    */

}
