package org.softauto.listener.impl;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.softauto.core.Utils;
import org.softauto.listener.ListenerClientProviderImpl;
import org.softauto.listener.LogBuilder;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class Logger {

    static org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Logger.class);
    static boolean isInit = false;
    static Object serviceImpl  ;
    static Method method;
    static String servicename = "tests.infrastructure.ListenerServiceLog";

    private  static ExecutorService executor = Executors.newFixedThreadPool(50);

    static void init(){
        try {
            serviceImpl = ListenerClientProviderImpl.getInstance().getServiceImpl();
            method = serviceImpl.getClass().getDeclaredMethod("execute", new Class[]{String.class,Object[].class,Class[].class,java.lang.String.class});
        }catch (Exception e){
            logger.error("ServiceImpl not found ",e);
        }
    }

    public static void javaLoggingLog(JoinPoint joinPoint) throws Throwable {
       log("INFO","SUT",(String)joinPoint.getArgs()[0],((java.util.logging.Logger)joinPoint.getTarget()).getName(),null);
    }

    public static void log4jinfo(JoinPoint joinPoint) throws Throwable {
       log("INFO","SUT",(String)joinPoint.getArgs()[0],((org.apache.log4j.Logger)joinPoint.getTarget()).getName(),null);
    }

    public static void slf4jinfo(JoinPoint joinPoint) throws Throwable {
       log("INFO","SUT",(String)joinPoint.getArgs()[0],((org.slf4j.Logger)joinPoint.getTarget()).getName(),null);
    }

    public static  void jog4j2info(JoinPoint joinPoint) throws Throwable {
       log("INFO","SUT",(String)joinPoint.getArgs()[0],((org.apache.log4j.Logger)joinPoint.getTarget()).getName(),null);
    }

    public static void log4jerror(JoinPoint joinPoint) throws Throwable {
       log("ERROR","SUT",(String)joinPoint.getArgs()[0],((org.apache.log4j.Logger)joinPoint.getTarget()).getName(),Utils.printStackTrace((Throwable)joinPoint.getArgs()[1]));
    }

    public static  void log4j2debug(JoinPoint joinPoint) throws Throwable {
       log("DEBUG","SUT",(String)joinPoint.getArgs()[0],((org.apache.logging.log4j.Logger)joinPoint.getTarget()).getName(),null);
    }

    public static  void log4j2info(JoinPoint joinPoint) throws Throwable {
       log("INFO","SUT",(String)joinPoint.getArgs()[0],((org.apache.logging.log4j.Logger)joinPoint.getTarget()).getName(),null);
    }

    public static  void log4j2warn(JoinPoint joinPoint) throws Throwable {
       log("WARN" ,"SUT",(String)joinPoint.getArgs()[0],((org.apache.logging.log4j.Logger)joinPoint.getTarget()).getName(),null);
    }

    public static  void log4j2fatal(JoinPoint joinPoint) throws Throwable {
       log("FATAL","SUT",(String)joinPoint.getArgs()[0],((org.apache.logging.log4j.Logger)joinPoint.getTarget()).getName(),null);
    }

    public static  void log4j2error(JoinPoint joinPoint) throws Throwable {
       log("ERROR","SUT",(String)joinPoint.getArgs()[0],((org.apache.logging.log4j.Logger)joinPoint.getTarget()).getName(),Utils.printStackTrace((Throwable)joinPoint.getArgs()[1]));
    }

    public static  void slf4jerror(JoinPoint joinPoint) throws Throwable {
       log("ERROR","SUT",(String)joinPoint.getArgs()[0],((org.slf4j.Logger)joinPoint.getTarget()).getName(),Utils.printStackTrace((Throwable)joinPoint.getArgs()[1]));
    }

    public static  void log4jdebug(JoinPoint joinPoint) throws Throwable {
       log("DEBUG","SUT",(String)joinPoint.getArgs()[0],((org.apache.log4j.Logger)joinPoint.getTarget()).getName(),null);
    }

    public static  void slf4jdebug(JoinPoint joinPoint) throws Throwable {
       log("DEBUG","SUT",(String)joinPoint.getArgs()[0],((org.slf4j.Logger)joinPoint.getTarget()).getName(),null);
    }

    public static  void softautoLoggerLog(JoinPoint joinPoint) throws Throwable {
       log(((org.apache.logging.log4j.Level)joinPoint.getArgs()[0]).name(),((org.apache.logging.log4j.Marker)joinPoint.getArgs()[1]).getName(),(String)joinPoint.getArgs()[2],((Class)joinPoint.getArgs()[3]).getName(),null);
    }

    public  static void log(String level,String marker,String log,String clazz,String ex) {
        if (!isInit) {
            init();
            isInit = true;
        }
        try {
            if (ex == null) {
                String fqmn = "org_auto_tests_log";
                LogBuilder logBuilder = LogBuilder.newBuilder().setArguments(level, marker, log, clazz).build();
                executor.submit(() -> {
                    try {
                        logger.debug("fqmn:" + fqmn + " " + logBuilder.toJson());
                        method.setAccessible(true);
                        method.invoke(null, new Object[]{fqmn,logBuilder.getArguments(), logBuilder.getClasses(), servicename});
                    } catch (Exception e) {
                        logger.error("send message org_auto_tests_log fail ", e);
                    }
                });
            } else {
                String fqmn = "org_auto_tests_logError";
                LogBuilder logBuilder = LogBuilder.newBuilder().setArguments(level, marker, log, clazz, ex).build();
                executor.submit(() -> {
                    try {
                        logger.debug("fqmn:" + fqmn + " " + logBuilder.toJson());
                        method.setAccessible(true);
                        method.invoke(null, new Object[]{fqmn,logBuilder.getArguments(), logBuilder.getClasses(), servicename});
                    } catch (Exception e) {
                        logger.error("send message org_auto_tests_logError fail ", e);
                    }
                });
            }
        } catch (Exception e) {
            logger.error("capture log  fail ", e);
        }
    }

}
