package org.softauto.tester;

import org.softauto.serializer.CallFuture;
import org.softauto.plugin.ProviderManager;
import org.softauto.plugin.api.Provider;
import java.util.Arrays;

public class InvocationHandler {

    private  org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(InvocationHandler.class);

    public Object invoke(String methodName, Object[] args, Class[] types, CallFuture callback,String transceiver)  {
        try {
            Provider provider = ProviderManager.provider(transceiver).create();
            logger.debug("invoke method " + methodName+ " using protocol "+ transceiver);
            provider.exec( methodName, callback,null,new Object[]{args,types});
        } catch (Exception e) {
            logger.error("fail invoke method "+ methodName+ " with args "+ Arrays.toString(args),e);
        }
        return null;
    }




}
