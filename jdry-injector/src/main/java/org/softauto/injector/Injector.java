package org.softauto.injector;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.core.AbstractInjector;
import org.softauto.core.ClassType;
import org.softauto.core.Utils;

/**
 * Injector  new class instance
 */
public class Injector implements AbstractInjector {

    private static final Logger logger = LogManager.getLogger(Injector.class);
    private ServiceDefinition service ;


    public  Injector createServiceDefinition(Class iface){
        service =  ServerService.createServiceDefinition(iface);
        return this;
    }

    /**
     * update ClassDescriptor with request arg value at runtime
     * @param fullClassName
     * @param values
     */
    public void UpdateClassDescriptorArgsValues(String fullClassName,Object[] values){
        ClassDefinition md = service.getClazz(fullClassName);
        ClassDescriptor cd =  md.getClassDescriptor();
        cd.setArgs(values);
    }

    /**
     * create new class instance
     * @param fullClassName
     * @return
     */
    public  Object[] inject(String fullClassName){
        Object[] objs = null;
        try {
            if(service != null && service.isExist(fullClassName)) {
                ClassDefinition md = service.getClazz(fullClassName);
                if (md != null) {
                    objs = md.getCallHandler().startCall(md.getClassDescriptor());
                    logger.debug("successfully inject " + fullClassName);
                }
            }else {
                logger.warn("Class Definition for " + fullClassName + " not found . using temporary service with INITIALIZE_NO_PARAM");
                ServiceDefinition serviceDefinition = ServerService.createServiceDefinition(fullClassName);
                ClassDefinition md1 =  serviceDefinition.getClazz(fullClassName);
                if(md1 != null) {
                    objs = md1.getCallHandler().startCall(md1.getClassDescriptor());
                    logger.debug("successfully inject " + fullClassName);
                }
            }
         }catch (Exception e){
            logger.error("fail inject "+ fullClassName,e);
        }
        return objs;
    }

    public  Object[] inject(String fullClassName,Object[] args){
        try {
            ClassDefinition md = service.getClazz(fullClassName);
            ClassDescriptor cd = md.getClassDescriptor();
            Object[] objs = md.getCallHandler().startCall(cd,args);
            logger.debug("successfully inject "+ fullClassName + " with args "+ Utils.result2String(args));
            return objs;
        }catch (Exception e){
            logger.error("fail inject "+ fullClassName+ " with args "+ Utils.result2String(args),e);
        }
        return null;
    }

}
