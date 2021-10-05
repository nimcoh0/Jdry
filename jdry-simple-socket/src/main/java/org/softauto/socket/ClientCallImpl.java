package org.softauto.socket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


/**
 * simple org.softauto.socket client
 */
public class ClientCallImpl {

    Socket clientSocket;
    DataOutputStream out;
    DataInputStream in;
    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(ClientCallImpl.class);


    public ClientCallImpl(String host, int port){
        try {
             clientSocket = new Socket(host, port);
             clientSocket.setSendBufferSize(10000);
             out = new DataOutputStream(clientSocket.getOutputStream());
             in = new DataInputStream(clientSocket.getInputStream());
             logger.debug("init Client socket successfully ");
        }catch (Exception e){
            logger.error("fail init socket Client ",e);
        }
    }


    /**
     * send org.softauto.socket message
     * @param message
     * @param numberOfByteToRead back
     * @return
     */
    public  byte[] sendMessage(String  message,int numberOfByteToRead) {
        byte[] b = new byte[numberOfByteToRead];
        try {
            out.write(binary(message).array());
            in.read(b);
            out.flush();
            logger.debug("message :"+message+ " with number Of Byte To Read "+ numberOfByteToRead + "exec successfully");
        } catch (Exception e) {
            logger.error("message :"+message+ " with number Of Byte To Read "+ numberOfByteToRead + " fail exec ");
        }finally {
            try {
                clientSocket.close();
                in.close();
                out.close();
            } catch (IOException e) {
               logger.error("fail close socket", e);
            }
        }
        return b;
    }





    private ByteBuf binary(Object... data)
    {
        return Unpooled.copiedBuffer(parseHex(concatenateStrings(data)));
    }

    private String concatenateStrings(Object... strings) {
        StringBuilder builder = new StringBuilder();
        for (Object s : strings) {
            builder.append(s);
        }
        return builder.toString();
    }

    private  byte[] parseHex(String string) {
        try {
            return Hex.decodeHex(string);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
