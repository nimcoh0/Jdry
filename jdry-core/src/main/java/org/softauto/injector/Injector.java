package org.softauto.injector;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.core.ClassType;


/**
 * Injector  new class instance
 */
public class Injector  {

    private static final Logger logger = LogManager.getLogger(Injector.class);

    /**
     * create new class instance
     * @param fullClassName
     * @return
     */
    public static Object[] inject(String fullClassName, ClassType classType,Object[] args,Class[] types){
        try {
            switch (classType) {
                case INITIALIZE_NO_PARAM: return ServerService.InitializeNoParamClassHandler(fullClassName);
                case INITIALIZE: return ServerService.InitializeClassHandler(fullClassName,args,types);
                case SINGLETON: return ServerService.SingletonClassHandler(fullClassName);
                default: return ServerService.NoneHandler(fullClassName);
            }

        }catch (Exception e){
            logger.error("fail inject "+ fullClassName,e);
        }
        return null;
    }


}
