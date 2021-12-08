package tests;

import org.junit.Assert;
import org.softauto.serializer.CallFuture;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import tests.infrastructure.AbstractTesterImpl;
import tests.infrastructure.Listener;
import tests.infrastructure.Step;

@Listeners(org.softauto.testng.JdryTestListener.class)
public class SocketExampleTests extends AbstractTesterImpl {

    @Test
    public void sendMessage()throws  Exception{
      Object result =   new Step.helper_Sock_send("hello").get_Result();
      Assert.assertTrue(result.toString().equals("ok"));
    }

    @Test
    public void send_and_listen() throws  Exception{
        CallFuture<Object[]> future = new CallFuture<>();
        new Step.helper_Sock_send("hello").then(
                Listener.com_baeldung_socket_EchoMultiServer_echo.waitTo(future), res ->{
                    Assert.assertTrue(res.result().toString().equals("ok") );
                    Assert.assertTrue(future.getResult()[0].toString().equals("hello"));
                }
        );
    }

}
