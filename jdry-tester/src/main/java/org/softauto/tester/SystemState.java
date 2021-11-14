package org.softauto.tester;

import com.fasterxml.jackson.databind.JsonNode;
import org.softauto.core.AsyncResult;
import org.softauto.core.Configuration;
import org.softauto.core.Future;
import org.softauto.core.Handler;

import java.io.IOException;


public class SystemState {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(SystemState.class);

    private static SystemState systemState = null;

    private SystemState(){};

    public static SystemState getInstance(){
        if(systemState == null){
            systemState = new SystemState();
        }
        return systemState;
    }


    public void initialize() throws IOException {
        sayHello(res->{
           if(res.succeeded()){
               logger.debug("successfully say hello");
               sendConfiguration(result ->{
                   if(result.succeeded()){
                       logger.debug("successfully send configuration");
                   }else {
                       logger.error("fail send configuration ",result.cause());
                   }
               });
           }else {
               logger.error("fail say hello ",res.cause());
           }
        });
    }


    private void sayHello(Handler<AsyncResult<Integer>> resultHandler){
        int result = new InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_hello", new Object[]{}, new Class[]{});
        if (Integer.valueOf(result) == 0) {
            resultHandler.handle(Future.handleResult(Integer.valueOf(result)));
        }else
            resultHandler.handle(Future.handleError(new Exception("fail sayHello")));
    }

    public void sendConfiguration(Handler<AsyncResult<Integer>> resultHandler){
        int result = new InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_configuration", new Object[]{Configuration.getConfiguration()}, new Class[]{JsonNode.class});
        if (Integer.valueOf(result) == 0) {
            resultHandler.handle(Future.handleResult(Integer.valueOf(result)));
        }else
            resultHandler.handle(Future.handleError(new Exception("fail send configuration")));
    }

    public void shutdown(Handler<AsyncResult<Boolean>> resultHandler) {
        new InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_shutdown", new Object[]{}, new Class[]{});
        resultHandler.handle(Future.handleResult(true));
    }

    public void startTest(String testname,Handler<AsyncResult<Boolean>> resultHandler){
        new InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_startTest", new Object[]{testname}, new Class[]{String.class});
        resultHandler.handle(Future.handleResult(true));
    }

    public void endTest(String testname,Handler<AsyncResult<Boolean>> resultHandler){
        new InvocationHandler().invoke("org_softauto_system_SystemServiceImpl_endTest", new Object[]{testname}, new Class[]{String.class});
        resultHandler.handle(Future.handleResult(true));
    }


}
