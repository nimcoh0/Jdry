package org.softauto.logger;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;


/**
 *  logger manager ,keep track of class per logger to be use in the log message
 */
public class LogManager {

    private static HashMap<Class,Logger> loggers = new HashMap<>();
    private static boolean status = false;
    private static final Logger logger = getLogger(LogManager.class);


    public static Logger getLogger(final Class<?> clazz) {
        Logger logger = new Logger();
        if(logger != null && clazz != null) {
            loggers.put(clazz, logger);
            return logger;
        }
        logger.error("fail to get logger for class for "+clazz);
        return null;
    }


    public static Class<?> getLoggerClass(Logger logger){
        try{
            AtomicReference<Class> ref = new AtomicReference<>();
            loggers.forEach((k,v)->{
                if(v.equals(logger)){
                    ref.set(k);
                }
            });
            return ref.get();
        }catch (Exception e){
            logger.error("fail to get logger for "+logger,e.getMessage());
        }
        return null;
    }

    public static boolean isStatus() {
        return status;
    }

    public static void setStatus(boolean status) {
        LogManager.status = status;
    }
}
