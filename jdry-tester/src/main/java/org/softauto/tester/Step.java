/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package org.softauto.tester;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.core.AsyncResult;
import org.softauto.core.Future;
import org.softauto.core.Handler;
import org.softauto.core.IListener;
import org.softauto.serializer.CallFuture;

@org.apache.avro.specific.AvroGenerated
public class Step {

    private static Logger logger = LogManager.getLogger(Step.class);
    CallFuture<Object> future = null;
    public Step(){};

    public Object get_Result() throws Exception{
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


    public Step(String fqmn, Object[] args, Class[] types, String transceiver)throws Exception{
        future = new CallFuture<>();
        logger.debug("invoking " +fqmn);
        new InvocationHandler().invoke(fqmn,args,types,future,transceiver);
    }

    public <T> Step(String fqmn, Object[] args, Class[] types, String transceiver, CallFuture<T> future)throws Exception{
        logger.debug("invoking " +fqmn);
        new InvocationHandler().invoke(fqmn,args,types,future,transceiver);
    }

    public <T>Step(String fqmn, Object[] args, Class[] types, String transceiver, Handler<AsyncResult<T>> resultHandler)throws Exception{
        CallFuture<Object> future = new CallFuture<>();
        logger.debug("invoking " +fqmn);
        new InvocationHandler().invoke(fqmn,args,types,future,transceiver);
        resultHandler.handle(Future.handleResult((T)future.get()));
    }


        public <T> Step then(Handler<AsyncResult<T>> resultHandler)throws Exception{
            resultHandler.handle(Future.handleResult((T)future.getResult()));
            return this;
        }


        public Step then(IListener o)throws Exception{
            future.handleResult(future.getResult());
            return this;
        }

        public <T> Step then(IListener o, Handler<AsyncResult<T>> resultHandler)throws Exception{
            resultHandler.handle(Future.handleResult((T)future.getResult()));
            return this;
        }

        public <T> Step then(IListener o , CallFuture<T> future)throws Exception{
            future.handleResult(future.getResult());
            return this;
        }

}