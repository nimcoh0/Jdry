package org.softauto.grpc.listener;

import org.apache.avro.Protocol;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.softauto.core.Configuration;
import org.softauto.core.Context;
import org.softauto.core.Utils;
import org.softauto.grpc.SoftautoGrpcUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * inspection class
 */
@Aspect
public class Client {


    org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Client.class);

    /** marker for trace logs */
    private static final Marker TRACER = MarkerManager.getMarker("TRACER");

    /** status of connection to the listener server */
    boolean connected  = false;

    /** status of sending  jdry logs to tester **/
    boolean sendJdryLogs = false;

    /** status of sending  sut logs to tester **/
    boolean sendSutLogs = false;


    private ExecutorService executor = Executors.newFixedThreadPool(50);

    /** list of classes to be ignore in the logs */
    List<String> logIgnoreList = new ArrayList();

    String listenerService;
    String listenerServiceLog;
    Class listener;
    Class log;

    public void init() {
        try {
            listener = Utils.getRemoteOrLocalClass(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText() , Context.LISTENER_SERVICE,Configuration.get(Context.TEST_MACHINE).asText());
            listenerService = SoftautoGrpcUtils.getServiceName(listener);
            log = Utils.getRemoteOrLocalClass(Configuration.get(Context.TEST_INFRASTRUCTURE_PATH).asText() ,Context.LISTENER_SERVICE_LOG,Configuration.get(Context.TEST_MACHINE).asText());
            listenerServiceLog = SoftautoGrpcUtils.getServiceName(listener);
            sendJdryLogs = Configuration.get("log_send_jdry").asBoolean();
            sendSutLogs = Configuration.get("log_send_sut").asBoolean();
            logIgnoreList = Arrays.asList(Configuration.get("log_trace_ignore_list").asText().split(","));
            logger.debug("init finish successfully " );
        }catch(Throwable e){
            logger.error("start Client fail ",e );
        }
    }


        /**
        * capture all methods in the local domain and send  Signature
        */
        @Around("execution(* *(..)) && !within(org.softauto..*)")
        public Object captureAll(ProceedingJoinPoint joinPoint){
               Object result =null;
               AtomicReference<String> fqmn = new AtomicReference();
               try {
                   MethodSignature sig = (MethodSignature) joinPoint.getSignature();
                   fqmn.set(Utils.buildMethodFQMN(sig.getName(),sig.getDeclaringType().getName()));
                   logger.trace(TRACER,"IN " + fqmn.get() +"( "+  Arrays.toString(sig.getMethod().getParameterTypes())+")[" +Arrays.toString(joinPoint.getArgs())+"]");
                   AtomicReference<Object[]> ref = new AtomicReference();
                   executor.submit(()-> {
                   try {
                     if(this.connected) {
                         if (listener.getMethod(fqmn.get(), sig.getMethod().getParameterTypes()) != null) {
                             Sender sender = Sender.newBuilder().setService(listenerService).setFqmn(fqmn.get()).setObjs(new Object[]{joinPoint.getArgs(), sig.getMethod().getParameterTypes()}).build();
                             logger.debug("fqmn:" + fqmn.get() + " args:" + joinPoint.getArgs().toString() + " types:" + sig.getMethod().getParameterTypes());
                             if (sender != null) {
                                 ref.set(sender.send());
                             } else {
                                 ref.set(new Object[]{});
                                 logger.error("send message " + fqmn.get() + " fail  ");
                             }
                         }
                     }
                   }catch(Exception e){
                        logger.error("send message "+fqmn.get()+" fail  ",e );
                   }
                   }).get();
                   Object[] o = ref.get();
                   if(o != null  && o.length > 0  && o[0] != null && o[0].toString().equals("result:")){
                         result = o[1];
                   }else {
                   if(o != null  && o.length > 0 && o[0] != null){
                          result = joinPoint.proceed(o);
                       }else {
                          result = joinPoint.proceed();
                       }
                   }
                   logger.trace(TRACER,"OUT " + fqmn.get() +" ("+  sig.getReturnType()+") "+Utils.result2String(result,logIgnoreList) );
                } catch (Throwable e) {
                    logger.error("capture message "+fqmn.get()+" fail  ",e );
                    logAfterThrowingAllMethodsSend(e);
           	    }

                return result;
             }


           @Before("call(* java.util.logging.Logger.log(..)) && !within(org.softauto..*)")
           public void advice(JoinPoint joinPoint) throws Throwable {
           if(sendSutLogs)
           	     log("INFO","SUT",(String)joinPoint.getArgs()[0],((java.util.logging.Logger)joinPoint.getTarget()).getName(),null);
           }

           @Before("call(* org.apache.log4j.Logger.info(..)) && !within(org.softauto..*)")
           public void log4jinfo(JoinPoint joinPoint) throws Throwable {
           if(sendSutLogs)
           	     log("INFO","SUT",(String)joinPoint.getArgs()[0],((org.apache.log4j.Logger)joinPoint.getTarget()).getName(),null);
           }

           @Before("call(* org.slf4j.Logger.info(..)) && !within(org.softauto..*)")
	       public void slf4jinfo(JoinPoint joinPoint) throws Throwable {
	       if(sendSutLogs)
	             log("INFO","SUT",(String)joinPoint.getArgs()[0],((org.slf4j.Logger)joinPoint.getTarget()).getName(),null);
	       }

           @Before("call(* org.apache.logging.log4j.Logger.log(..)) && !within(org.softauto..*)")
	       public void jog4j2info(JoinPoint joinPoint) throws Throwable {
	       if(sendSutLogs)
	             log("INFO","SUT",(String)joinPoint.getArgs()[0],((org.apache.log4j.Logger)joinPoint.getTarget()).getName(),null);
	       }

           @Before("call(* org.apache.log4j.Logger.error(..)) && !within(org.softauto..*)")
           public void log4jerror(JoinPoint joinPoint) throws Throwable {
           if(sendSutLogs)
           	     log("ERROR","SUT",(String)joinPoint.getArgs()[0],((org.apache.log4j.Logger)joinPoint.getTarget()).getName(),Utils.printStackTrace((Throwable)joinPoint.getArgs()[1]));
           }

           @Before("call(* org.apache.logging.log4j.Logger.debug(..)) && !within(org.softauto..*)")
           public void log4j2debug(JoinPoint joinPoint) throws Throwable {
           if(sendSutLogs)
           	       log("DEBUG","SUT",(String)joinPoint.getArgs()[0],((org.apache.logging.log4j.Logger)joinPoint.getTarget()).getName(),null);
           }

	       @Before("call(* org.apache.logging.log4j.Logger.info(..)) && !within(org.softauto..*)")
           public void log4j2info(JoinPoint joinPoint) throws Throwable {
           if(sendSutLogs)
           	       log("INFO","SUT",(String)joinPoint.getArgs()[0],((org.apache.logging.log4j.Logger)joinPoint.getTarget()).getName(),null);
           }

           @Before("call(* org.apache.logging.log4j.Logger.warn(..)) && !within(org.softauto..*)")
           public void log4j2warn(JoinPoint joinPoint) throws Throwable {
           if(sendSutLogs)
                    log("WARN" ,"SUT",(String)joinPoint.getArgs()[0],((org.apache.logging.log4j.Logger)joinPoint.getTarget()).getName(),null);
           }

           @Before("call(* org.apache.logging.log4j.Logger.fatal(..)) && !within(org.softauto..*)")
           public void log4j2fatal(JoinPoint joinPoint) throws Throwable {
           if(sendSutLogs)
                    log("FATAL","SUT",(String)joinPoint.getArgs()[0],((org.apache.logging.log4j.Logger)joinPoint.getTarget()).getName(),null);
           }

           @Before("call(* org.apache.logging.log4j.Logger.error(..)) && !within(org.softauto..*)")
           public void log4j2error(JoinPoint joinPoint) throws Throwable {
           if(sendSutLogs)
                    log("ERROR","SUT",(String)joinPoint.getArgs()[0],((org.apache.logging.log4j.Logger)joinPoint.getTarget()).getName(),Utils.printStackTrace((Throwable)joinPoint.getArgs()[1]));
           }

           @Before("call(* org.slf4j.Logger.error(..)) && !within(org.softauto..*)")
	       public void slf4jerror(JoinPoint joinPoint) throws Throwable {
	       if(sendSutLogs)
	                log("ERROR","SUT",(String)joinPoint.getArgs()[0],((org.slf4j.Logger)joinPoint.getTarget()).getName(),Utils.printStackTrace((Throwable)joinPoint.getArgs()[1]));
	       }

           @Before("call(* org.apache.log4j.Logger.debug(..)) && !within(org.softauto..*)")
           public void log4jdebug(JoinPoint joinPoint) throws Throwable {
           if(sendSutLogs)
                    log("DEBUG","SUT",(String)joinPoint.getArgs()[0],((org.apache.log4j.Logger)joinPoint.getTarget()).getName(),null);
            }

           @Before("call(* org.slf4j.Logger.debug(..)) && !within(org.softauto..*)")
           public void slf4jdebug(JoinPoint joinPoint) throws Throwable {
           if(sendSutLogs)
             	   log("DEBUG","SUT",(String)joinPoint.getArgs()[0],((org.slf4j.Logger)joinPoint.getTarget()).getName(),null);
           }

           @Before("execution(* org.softauto.logger.Logger.log(..)) ")
	       public void softautoLoggerLog(JoinPoint joinPoint) throws Throwable {
	       if(sendJdryLogs)
	                log(((org.apache.logging.log4j.Level)joinPoint.getArgs()[0]).name(),((org.apache.logging.log4j.Marker)joinPoint.getArgs()[1]).getName(),(String)joinPoint.getArgs()[2],((Class)joinPoint.getArgs()[3]).getName(),null);
            }



            @Before("execution(* org.softauto.system.SystemServiceImpl.setConnection(..)) ")
            public void setConnection(JoinPoint joinPoint) throws Throwable {
                Object[] objs = joinPoint.getArgs();
                connected = ((Boolean)objs[0]);
                if(connected){
                    init();
                }
                logger.debug("connection set to "+connected );
            }



	       public void log(String level,String marker,String log,String clazz,String ex){
          	            try {
          	                 if(ex == null){
                                  String fqmn = "org_auto_tests_log";
                                  LogBuilder logBuilder = LogBuilder.newBuilder().setArguments(level,marker,log,clazz).build();
                                  executor.submit(()-> {
                                  try {
                                      if(this.connected) {
                                          Sender sender = Sender.newBuilder().setService(listenerServiceLog).setFqmn(fqmn).setObjs(new Object[]{logBuilder.getArguments(), logBuilder.getClasses()}).build();
                                          if(sender != null) {
                                              sender.send();
                                          }
                                      }
                                    }catch(Exception e){
                                        logger.error("send message org_auto_tests_log fail ",e );
                                     }
                                  }).get();
                              }else {
                                 String fqmn = "org_auto_tests_logError";
                                 LogBuilder logBuilder = LogBuilder.newBuilder().setArguments(level,marker,log,clazz,ex).build();
                                 executor.submit(()-> {
                                 try {
                                      if(this.connected) {
                                         Sender sender = Sender.newBuilder().setService(listenerServiceLog).setFqmn(fqmn).setObjs(new Object[]{logBuilder.getArguments(), logBuilder.getClasses()}).build();
                                         if(sender != null) {
                                             sender.send();
                                         }
                                     }
                                     }catch(Exception e){
                                        logger.error("send message org_auto_tests_logError fail ",e );
                                    }
                                 }).get();
                              }

                          }catch(Exception e){
                                logger.error("capture log  fail ",e );
                          }
          	      }


        @AfterThrowing (pointcut = "execution(* *(..))", throwing = "e")
        public void logAfterThrowingAllMethods(JoinPoint joinPoint,Exception e)
        {
            try {
                logAfterThrowingAllMethodsSend(e);
            }catch(Exception ex){
                logger.error("capture exception fail  ",e );
           }
        }

        public void logAfterThrowingAllMethodsSend(Throwable e)
        {
            try {
                Object[] args = new Object[1];
                args[0] = Utils.printStackTrace(e);
                Class[] types = new Class[1];
                types[0] = String.class;
                String fqmn = "org_auto_tests_exception";
                if(sendSutLogs)
                    executor.submit(()-> {
                    try {
                       if(this.connected) {
                            Sender sender = Sender.newBuilder().setService(listenerServiceLog).setFqmn(fqmn).setObjs(new Object[]{args, types}).build();
                            if(sender != null) {
                               sender.send();
                            }
                        }
                         }catch(Exception e1){
                            logger.error("logAfterThrowingAllMethodsSend fail  " + fqmn,e );
                         }
                    }).get();

            }catch(Exception ex){
                logger.error("capture exception fail  ",e );
          }
        }

       @AfterReturning(pointcut = "execution(* *(..)) && !within(org.softauto..*)", returning = "result")
       public void sendResult(JoinPoint joinPoint, Object result){
            try{
                MethodSignature sig = (MethodSignature) joinPoint.getSignature();
                String fqmn = Utils.buildMethodFQMN(sig.getName(),sig.getDeclaringType().getName());
                if(!sig.getMethod().getReturnType().getName().equals("void") ){
                    if(result != null)
                        executor.submit(()-> {
                        try {
                           if(this.connected) {
                               if (listener.getMethod(fqmn, sig.getMethod().getReturnType()) != null) {
                                   Sender sender = Sender.newBuilder().setService(listenerService).setFqmn(fqmn + "_result").setObjs(new Object[]{result, sig.getMethod().getReturnType()}).build();
                                   if (sender != null) {
                                       sender.send();
                                   }
                               }
                            }
                             }catch(Exception e){
                                logger.error("sendResult fail for "+fqmn,e );
                             }
                        }).get();
                }else {
                    executor.submit(()-> {
                    try {
                        if(this.connected) {
                            if (listener.getMethod(fqmn, sig.getMethod().getParameterTypes()) != null) {
                                Sender sender = Sender.newBuilder().setService(listenerService).setFqmn(fqmn + "_result").setObjs(new Object[]{joinPoint.getArgs(), sig.getMethod().getParameterTypes()}).build();
                                if (sender != null) {
                                    sender.send();
                                }
                            }
                        }
                          }catch(Exception e){
                            logger.error("sendResult fail for "+fqmn ,e);
                          }
                    }).get();
                }

             }catch(Exception e){
               logger.error("capture result fail  ",e );
            }
       }
}