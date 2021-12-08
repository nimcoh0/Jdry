package com.baeldung.socket;

import org.apache.logging.log4j.Logger;
import org.softauto.annotations.ListenerForTesting;
import org.softauto.annotations.RPC;


import java.net.*;
import java.io.*;

public class EchoMultiServer {

    private static final Logger LOG = org.apache.logging.log4j.LogManager.getLogger(EchoMultiServer.class);

    private ServerSocket serverSocket;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            while (true)
                new EchoClientHandler(serverSocket.accept()).start();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stop();
        }

    }

    public void stop() {
        try {

            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static class EchoClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (".".equals(inputLine)) {
                        out.println("bye");
                        break;
                    }
                    echo(inputLine);
                    out.println("ok");
                }

                in.close();
                out.close();
                clientSocket.close();

            } catch (IOException e) {
                LOG.debug(e.getMessage());
            }
        }
    }

    @RPC
    @ListenerForTesting
    private static void echo(String message){
        System.out.print(message);
    }

    public static void main(String[] args) {
        EchoMultiServer server = new EchoMultiServer();
        server.start(5555);
    }

}
