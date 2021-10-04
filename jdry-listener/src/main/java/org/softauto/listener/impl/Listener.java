package org.softauto.listener.impl;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.softauto.core.Utils;
import org.softauto.listener.ListenerClientProviderImpl;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class Listener {

    static org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Listener.class);
    /** marker for trace logs */
    private static final Marker TRACER = MarkerManager.getMarker("TRACER");
    static boolean isInit = false;
    static Object serviceImpl  ;
    static Method method;
    static String servicename = "tests.infrastructure.ListenerService";

    private  static ExecutorService executor = Executors.newFixedThreadPool(50);

    static void init(){
        try {
           serviceImpl = ListenerClientProviderImpl.getInstance().getServiceImpl();
           method = serviceImpl.getClass().getDeclaredMethod("execute", new Class[]{String.class,Object[].class,Class[].class,java.lang.String.class});
        }catch (Exception e){
            logger.error("ServiceImpl not found ",e);
        }
    }


    public  static Object captureAll(org.aspectj.lang.ProceedingJoinPoint joinPoint){
        if(!isInit){
            init();
            isInit = true;
        }
        Object result =null;
        AtomicReference<String> fqmn = new AtomicReference();
        try {
            MethodSignature sig = (MethodSignature) joinPoint.getSignature();
            fqmn.set(Utils.buildMethodFQMN(sig.getName(),sig.getDeclaringType().getName()));
            logger.trace(TRACER,"IN " + fqmn.get() +"( "+  Arrays.toString(sig.getMethod().getParameterTypes())+")[" +Arrays.toString(joinPoint.getArgs())+"]");
            AtomicReference<Object[]> ref = new AtomicReference();
            executor.submit(() -> {
                    try {
                        logger.debug("fqmn:" + fqmn.get() + " args:" + joinPoint.getArgs().toString() + " types:" + sig.getMethod().getParameterTypes());
                        method.setAccessible(true);
                        ref.set((Object[]) method.invoke(null, new Object[]{fqmn.get(), joinPoint.getArgs(), sig.getMethod().getParameterTypes(),servicename}));
                    } catch (Exception e) {
                        logger.error("send message " + fqmn.get() + " fail  ", e);
                    }
                });
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
            logger.trace(TRACER,"OUT " + fqmn.get() +" ("+  sig.getReturnType()+") "+Utils.result2String(result) );
        } catch (Throwable e) {
            logger.error("capture message "+fqmn.get()+" fail  ",e );
        }
        returning(joinPoint,result);
        return result;
    }

    public  static void returning(JoinPoint joinPoint,Object result) {
        MethodSignature sig = (MethodSignature) joinPoint.getSignature();
        String fqmn = Utils.buildMethodFQMN(sig.getName(), sig.getDeclaringType().getName());
        if (!sig.getMethod().getReturnType().getName().equals("void")) {
            if (result != null)
                executor.submit(() -> {
                    try {
                        logger.debug("fqmn:" + fqmn + "_result" + " args:" + joinPoint.getArgs().toString() + " types:" + sig.getMethod().getParameterTypes());
                        method.setAccessible(true);
                        method.invoke(null, new Object[]{fqmn + "_result", new Object[]{result}, sig.getMethod().getReturnType(),servicename});
                    }catch(Exception e){
                        logger.error("sendResult fail for "+fqmn+ "_result",e );
                    }
                });
         }else {
            executor.submit(()-> {
                try {
                    logger.debug("fqmn:" + fqmn + "_result" + " args:" + joinPoint.getArgs().toString() + " types:" + sig.getMethod().getParameterTypes());
                    method.setAccessible(true);
                    method.invoke(null, new Object[]{fqmn+ "_result" , joinPoint.getArgs(), sig.getMethod().getParameterTypes(),servicename});

                }catch(Exception e){
                    logger.error("sendResult fail for "+fqmn+ "_result" ,e);
                }
            });
        }
    }






}
