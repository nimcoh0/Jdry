package org.softauto.socket;

import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;


/**
 * simple org.softauto.socket client
 */
public class SocketClient {

    private static final Logger LOG = org.apache.logging.log4j.LogManager.getLogger(SocketClient.class);

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public SocketClient startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            LOG.debug("successfully init socket "+ ip+":"+port);
        } catch (IOException e) {
            LOG.debug("Error when initializing connection", e);
        }
        return this;
    }


    public String sendMessage(String msg) {
        try {
            LOG.debug("sending msg "+ msg);
            out.println(msg);
            return in.readLine();
        } catch (Exception e) {
            LOG.error("fail sending msg ",e);
            return null;
        }
    }

    public SocketClient stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            LOG.debug("error when closing", e);
        }
        return this;
    }
}
