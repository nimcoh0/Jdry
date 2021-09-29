package org.softauto.listener;


import com.google.inject.Guice;
import com.google.inject.Injector;
import org.softauto.core.Utils;

import org.softauto.grpc.system.SystemServiceImpl;
import org.softauto.listener.system.BasicModule;
import org.softauto.serializer.CallFuture;
import org.softauto.serializer.service.Message;
import org.softauto.serializer.service.SerializerService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


public class SerializerServiceImpl implements SerializerService,SerializerService.Callback{

    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(SerializerServiceImpl.class);
    private Injector injector ;



    public SerializerServiceImpl(){
        try {
            injector = Guice.createInjector(new BasicModule());

            //injector = Guice.createInjector(new BasicModule());
        }catch (Exception e){
            e.printStackTrace();
        }
    }



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
}
