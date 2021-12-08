package helper;


import org.softauto.annotations.ExposedForTesting;
import org.softauto.core.ServiceLocator;
import org.softauto.local.annotations.LOCAL;
import org.softauto.plugin.api.Provider;
import org.softauto.serializer.CallFuture;
import tests.infrastructure.AbstractTesterImpl;


public class Sock {
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Sock.class);

    @LOCAL
    @ExposedForTesting
    public Object send(String message){
        org.softauto.serializer.CallFuture<String> callback = new CallFuture();
        try {
            Provider socket = (Provider) ServiceLocator.getInstance().getService("SOCKET");
            logger.debug("found provider for Socket service ");
            logger.debug("sending message "+ message);
            socket.exec("send", callback, null, new Object[]{message});
            return callback.get();
        }catch (Exception e){
            logger.error("fail execute local send ",e);
        }
       return null;

    }
}
