/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package tests.infrastructure;
import java.util.function.Function;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.softauto.core.AsyncResult;
import org.softauto.core.Handler;
import org.softauto.core.Future;
import org.softauto.serializer.CallFuture;
import org.softauto.tester.InvocationHandler;
import org.softauto.core.IListener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@org.apache.avro.specific.AvroGenerated
public class Step {

    private static org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Step.class);




public static class helper_Sock_send {

    CallFuture< java.lang.Object> future = new CallFuture<>();
    public java.lang.Object get_Result() throws Exception{
            try {

               if(!future.isDone()) {
                  logger.debug("waiting to future to be done");
                  future.await();
               }
                logger.debug("successfully get_Result() ");
                return future.get();
             }catch (Exception e){
                 logger.error("fail get_Result() "+ e);
                 throw new Exception("fail get_Result() "+ e);
             }
        }

    public  helper_Sock_send then(Handler<AsyncResult<java.lang.Object>> resultHandler)throws Exception{
        resultHandler.handle(Future.handleResult(future.get()));
        return this;
    }


    public  helper_Sock_send then(IListener o)throws Exception{
        future.handleResult(future.get());

        return this;
    }

    public  helper_Sock_send then(IListener o,Handler<AsyncResult<java.lang.Object>> resultHandler)throws Exception{
        resultHandler.handle(Future.handleResult(future.get()));
        return this;
    }

    public  helper_Sock_send then(IListener o ,CallFuture<java.lang.Object> future)throws Exception{
        future.handleResult(future.get());
        return this;
    }





    public helper_Sock_send(java.lang.String message    ){
        try {
            logger.debug("invoking proxy for helper_Sock_send");
            new InvocationHandler().invoke("helper_Sock_send",new Object[]{message},new Class[]{java.lang.String.class},future,"LOCAL");
        }catch (Exception e){
             logger.error("fail invoke for helper_Sock_send"+ e);
        }
    }



    public helper_Sock_send(java.lang.String message,CallFuture<java.lang.Object> future){
        try {
            logger.debug("invoking proxy for helper_Sock_send");
            new InvocationHandler().invoke("helper_Sock_send",new Object[]{message},new Class[]{java.lang.String.class},future,"LOCAL");
        }catch (Exception e){
             logger.error("fail invoke for helper_Sock_send"+ e);
        }
 }


    public  helper_Sock_send(java.lang.String message,Handler<AsyncResult<java.lang.Object>> resultHandler  )throws Exception{
        try {
            CallFuture<java.lang.Object> future = new CallFuture<>();
            logger.debug("invoking proxy for helper_Sock_send");
            new InvocationHandler().invoke("helper_Sock_send",new Object[]{message},new Class[]{java.lang.String.class},future,"LOCAL");
            resultHandler.handle(Future.handleResult(future.get()));
       }catch (Exception e){
          logger.error("fail invoke for helper_Sock_send"+ e);
          resultHandler.handle(Future.handleError(e));
       }
    }




 }}