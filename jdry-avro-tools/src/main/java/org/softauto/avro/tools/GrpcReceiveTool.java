package org.softauto.avro.tools;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.softauto.serializer.service.SerializerService;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;


public class GrpcReceiveTool implements Tool {

    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(GrpcReceiveTool.class);
    Server server;
    CountDownLatch lock = new CountDownLatch(1);

    @Override
    public int run(InputStream in, PrintStream out, PrintStream err, List<Object> arguments) throws Exception {
        try {

            if (arguments.size() != 6) {
                System.err.println(
                        "Usage: -port <port>  -impl <impl class>  ");
                System.err.println(" impl      - impl class file ");
                System.err.println(" port      - receiver port ");
                return 1;
            }

            Optional<Integer> port = Optional.empty();
            Optional<String> impl = Optional.empty();
            int arg = 0;
            List<Object> args = new ArrayList<>(arguments);


            if (args.contains("-port")) {
                arg = args.indexOf("-port") + 1;
                port = Optional.of(Integer.valueOf(args.get(arg).toString()));
                args.remove(arg);
                args.remove(arg - 1);
            }

            if (args.contains("-impl")) {
                arg = args.indexOf("-impl") + 1;
                impl = Optional.of(args.get(arg).toString());
                args.remove(arg);
                args.remove(arg - 1);
            }



            String path = getPath(impl.get());
            String className = getName(impl.get());
            Class iface = org.softauto.core.Utils.getClazz(path,className);

            server = ServerBuilder.forPort(port.get())
                    .addService(org.softauto.serializer.SoftautoGrpcServer.createServiceDefinition(SerializerService.class, iface.newInstance()))
                    .build();
            server.start();
            lock.await();

        }catch (Exception e){
            logger.error("fail send message ", e);
            return 1;
        }
        return 0;
    }

    public void shutdown(){
        server.shutdown();
        lock.countDown();
    }

    public String getPath(String p){
       return p.substring(0,p.lastIndexOf("/"));
    }

    public String getName(String p){
       return p.substring(p.lastIndexOf("/")+1);
    }

    @Override
    public String getName() {
        return "grpcreceive";
    }

    @Override
    public String getShortDescription() {
        return "Opens an GRPC Server and listens for one message.";
    }


}
