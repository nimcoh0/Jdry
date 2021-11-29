package org.softauto.injector;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.core.AbstractInjector;
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
        try {
            ClassDefinition md = service.getClazz(fullClassName);
            Object[] objs = md.getCallHandler().startCall(md.getClassDescriptor());
            logger.debug("successfully inject "+ fullClassName);
            return objs;
        }catch (Exception e){
            logger.error("fail inject "+ fullClassName,e);
        }
        return null;
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
