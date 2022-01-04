package org.softauto.logger.impl;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.softauto.core.*;
import org.softauto.serializer.Serializer;
import org.softauto.serializer.service.Message;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class Logger {

    static org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Logger.class);
    static boolean isInit = false;
    static Serializer serializer;

    private  static ExecutorService executor = Executors.newFixedThreadPool(50);

    static void init(){
        try {
            serializer = new Serializer().setHost(Configuration.get(Context.TEST_MACHINE)).setPort(Integer.valueOf(Configuration.get(Context.LISTENER_PORT))).build();

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

    public static void log4jerror(JoinPoint joinPoint) throws Throwable {
        log("ERROR","SUT",(String)joinPoint.getArgs()[0],((org.apache.log4j.Logger)joinPoint.getTarget()).getName(),Utils.printStackTrace((Throwable)joinPoint.getArgs()[1]));
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
        if (Context.getTestState().equals(TestLifeCycle.START)) {
            if (!isInit) {
                init();
                isInit = true;
            }
            try {
                if (Boolean.valueOf(Configuration.get(Context.SEND_LOG_TO_TESTER)) && serializer != null) {
                    if (clazz.contains("org.softauto") && !Boolean.valueOf(Configuration.get(Context.SEND_JDRY_LOG_TO_TESTER))) {
                        return;
                    }
                    if (ex == null) {
                        //String fqmn = "org_auto_tests_log";
                        //LogBuilder logBuilder = LogBuilder.newBuilder().setArguments(level, marker, log, clazz).build();
                        executor.submit(() -> {
                            try {
                                //Serializer serializer = new Serializer().setHost(Configuration.get(Context.TEST_MACHINE).asText()).setPort(Configuration.get(Context.LISTENER_PORT).asInt()).build();
                                Message message = Message.newBuilder().setState(ListenerType.LOG.name()).setDescriptor("log")
                                        .addData("level", level)
                                        .addData("marker", marker)
                                        .addData("log", log)
                                        .addData("clazz", clazz)
                                        //.addData(ex,ex)
                                        .setArgs(new Object[]{}).setTypes(new Class[]{}).build();
                                serializer.write(message);
                                //logger.debug("send message successfully " + methodName);


                                //logger.debug("fqmn:" + fqmn + " " + logBuilder.toJson());
                                //method.setAccessible(true);
                                //method.invoke(null, new Object[]{fqmn,logBuilder.getArguments(), logBuilder.getClasses(), servicename});
                            } catch (Exception e) {
                                logger.error("send message log fail ", e);
                            }
                        });
                    } else {
                        //String fqmn = "org_auto_tests_logError";
                        //LogBuilder logBuilder = LogBuilder.newBuilder().setArguments(level, marker, log, clazz, ex).build();
                        executor.submit(() -> {
                            try {
                                Message message = Message.newBuilder().setState(ListenerType.LOG.name()).setDescriptor("logError")
                                        .addData("level", level)
                                        .addData("marker", marker)
                                        .addData("log", log)
                                        .addData("clazz", clazz)
                                        .addData("ex", ex)
                                        .setArgs(new Object[]{}).setTypes(new Class[]{}).build();
                                serializer.write(message);


                                //logger.debug("fqmn:" + fqmn + " " + logBuilder.toJson());
                                //method.setAccessible(true);
                                //method.invoke(null, new Object[]{fqmn,logBuilder.getArguments(), logBuilder.getClasses(), servicename});
                            } catch (Exception e) {
                                logger.error("send message logError fail ", e);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                logger.error("capture log  fail ", e);
            }
        }
    }
}
