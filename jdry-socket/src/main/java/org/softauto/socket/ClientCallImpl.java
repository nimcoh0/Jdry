package org.softauto.socket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * simple org.softauto.socket client
 */
public class ClientCallImpl {

    private static SocketChannel client;
    private static ByteBuffer buffer;

    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(ClientCallImpl.class);


    public ClientCallImpl(String host, int port){
        try {
            client = SocketChannel.open(new InetSocketAddress(host, port));
            buffer = ByteBuffer.allocate(256);


             logger.debug("init Client socket successfully ");
        }catch (Exception e){
            logger.error("fail init socket Client ",e);
        }
    }



    public  String sendMessage(String  message) {
        buffer = ByteBuffer.wrap(message.getBytes());
        String response = null;
        try {
            client.write(buffer);
            buffer.clear();
            client.read(buffer);
            response = new String(buffer.array()).trim();
            logger.debug("message :"+message+ " with number Of Byte To Read exec successfully");
        } catch (Exception e) {
            logger.error("message :"+message+ " with number Of Byte To Read  fail exec ");
        }finally {
            try {
                buffer.clear();
            } catch (Exception e) {
               logger.error("fail close socket", e);
            }
        }
        return response;
    }




}
