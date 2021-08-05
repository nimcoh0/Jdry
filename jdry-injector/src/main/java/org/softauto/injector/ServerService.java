package org.softauto.injector;


import org.apache.avro.Protocol;
import org.apache.avro.Schema;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.core.ClassType;
import org.softauto.core.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * create the injector service
 * currently only Singleton,class with arg in the constructor & class with no arg in the constructor are supported
 */
public class ServerService {

    private static final Logger logger = LogManager.getLogger(ServerService.class);

    public static ServiceDefinition createServiceDefinition(Class iface) {
        ServiceDefinition.Builder serviceDefinitionBuilder = null;
        try {
            Protocol protocol = SoftautoGrpcUtils.getProtocol(iface);
            ServiceDescriptor serviceDescriptor = ServiceDescriptor.create(iface);
            serviceDefinitionBuilder = ServiceDefinition.builder(serviceDescriptor);
            Map<String, Protocol.Message> messages = protocol.getMessages();
            Collection<Schema> types = protocol.getTypes();
            for (Method method : iface.getMethods()) {
                Protocol.Message msg = messages.get(method.getName());
                // setup a method handler only if corresponding message exists in avro protocol.
                if (msg != null) {
                    if (!((HashMap) msg.getObjectProp("class")).get("fullClassName").toString().contains("$")) {
                        String initializeClass = null;
                        initializeClass = ((HashMap) msg.getObjectProp("class")).get("initialize").toString();
                        if (initializeClass == null) {
                            initializeClass = ClassType.NONE.toString();
                        }

                        if (initializeClass != null && initializeClass.equals(ClassType.SINGLETON.toString())) {
                            serviceDefinitionBuilder.addClass(serviceDescriptor.getClasses(ClassType.SINGLETON, msg),
                                    ServiceCaller.call(new SingletonClassHandler()));
                        } else if (initializeClass != null && initializeClass.equals(ClassType.INITIALIZE.toString())) {
                                    serviceDefinitionBuilder.addClass(serviceDescriptor.getClasses(msg, ClassType.INITIALIZE),
                                            ServiceCaller.call(new InitializeClassHandler()));
                              } else if (initializeClass != null && initializeClass.equals(ClassType.INITIALIZE_NO_PARAM.toString())) {
                                        serviceDefinitionBuilder.addClass(serviceDescriptor.getClasses(ClassType.INITIALIZE_NO_PARAM, msg),
                                                ServiceCaller.call(new InitializeNoParamClassHandler()));
                                    } else {
                                        serviceDefinitionBuilder.addClass(serviceDescriptor.getClasses(ClassType.NONE, msg),
                                                ServiceCaller.call(null));
                                        }


                    }else {
                        logger.error("inner class not supported " + ((HashMap) msg.getObjectProp("class")).get("fullClassName").toString());
                    }
                }
            }
        }catch (Throwable e){
            logger.error("fail create serviceDefinitionBuilder ",e);
        }
        return serviceDefinitionBuilder.build();
    }

    /**
     * Handler for class that we don't know how to load it
     */
    private static class NoneHandler implements ServiceCaller.UnaryClass  {
        @Override
        public Object[] invoke(ClassDescriptor classDescriptor) {
            logger.error("Class not loaded by the SUT and no info for auto loading for  "+classDescriptor.getFullClassName());
            return new Object[]{};
        }
    }


    /**
     * Handler for Singleton class
     */
    private static class SingletonClassHandler implements ServiceCaller.UnaryClass  {

        @Override
        public Object[] invoke(ClassDescriptor classDescriptor) {
            Object obj = null;
            try {
                Class c = Class.forName(classDescriptor.getFullClassName());
                Method singleton =  c.getDeclaredMethod("getInstance");
                obj  = singleton.invoke(c);

             }catch (Exception e){
                logger.warn("fail get Instance Class for  "+classDescriptor.getFullClassName(),e.getMessage());

            }
            return new Object[]{obj};
        }
    }



    /**
     * Handler for class with arg in the constructor
     */
    private static class InitializeClassHandler implements ServiceCaller.UnaryClass  {

        @Override
        public Object[] invoke(ClassDescriptor classDescriptor) {
            Object obj = null;
            try {
                Class c = Class.forName(classDescriptor.getFullClassName());
                Class[] types =  classDescriptor.getTypes();
                Object[] values = classDescriptor.getArgs();
                Constructor constructor = c.getDeclaredConstructor(types);
                obj  = constructor.newInstance(values);

            }catch (Exception e){
                logger.warn("fail get Instance Class for  "+classDescriptor.getFullClassName(),e.getMessage());
            }
            return new Object[]{obj};
        }
    }

    /**
     * Handler for class with no arg in the constructor
     */
    private static class InitializeNoParamClassHandler implements ServiceCaller.UnaryClass  {

        @Override
        public Object[] invoke(ClassDescriptor classDescriptor) {
            Object obj = null;
            try {
                Class c = Utils.findClass(classDescriptor.getFullClassName()) ;
                obj  = c.newInstance();

            }catch (Exception e){
                logger.warn("fail get Instance Class for  "+classDescriptor.getFullClassName(),e.getMessage());
            }
            return new Object[]{obj};
        }
    }


}