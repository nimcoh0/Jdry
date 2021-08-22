package org.softauto.injector;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.core.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * create the injector service
 * currently only Singleton,class with arg in the constructor & class with no arg in the constructor are supported
 */
public class ServerService {

    private static final Logger logger = LogManager.getLogger(ServerService.class);



    /**
     * Handler for class that we don't know how to load it
     */
    public static Object[] NoneHandler(String fullClassName)   {
        logger.error("Class not loaded by the SUT and no info for auto loading for  "+fullClassName);
        return new Object[]{};
    }


    /**
     * Handler for Singleton class
     */
    public static Object[] SingletonClassHandler(String fullClassName)   {
            Object obj = null;
            try {
                Class c = Class.forName(fullClassName);
                Method singleton =  c.getDeclaredMethod("getInstance");
                obj  = singleton.invoke(c);

             }catch (Exception e){
                logger.warn("fail get Instance Class for  "+fullClassName,e.getMessage());

            }
            return new Object[]{obj};
        }




    /**
     * Handler for class with arg in the constructor
     */
    public static Object[] InitializeClassHandler (String fullClassName,Object[] args,Class[] types)  {
            Object obj = null;
            try {
                Class c = Class.forName(fullClassName);
                Constructor constructor = c.getDeclaredConstructor(types);
                obj  = constructor.newInstance(args);

            }catch (Exception e){
                logger.warn("fail get Instance Class for  "+fullClassName,e.getMessage());
            }
            return new Object[]{obj};
        }


    /**
     * Handler for class with no arg in the constructor
     */
    public static Object[] InitializeNoParamClassHandler(String fullClassName) {
            Object obj = null;
            try {
                Class c = Utils.findClass(fullClassName) ;
                obj  = c.newInstance();

            }catch (Exception e){
                logger.warn("fail get Instance Class for  "+fullClassName,e.getMessage());
            }
            return new Object[]{obj};
    }
}

