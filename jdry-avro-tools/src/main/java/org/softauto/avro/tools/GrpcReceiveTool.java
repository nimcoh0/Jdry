package org.softauto.avro.tools;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.apache.avro.Protocol;
import org.softauto.grpc.SoftautoGrpcServer;
import org.softauto.grpc.SoftautoGrpcUtils;
//import org.softauto.avro.tools.service.AvroGrpcServer;
//import org.softauto.avro.tools.service.AvroGrpcUtils;
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
                        "Usage: -port <port>  -protocol_file <protocol file> -message_name <message name> ");
                System.err.println(" port               - receiver port ");
                System.err.println(" protocol_file      - protocol json file ");
                //System.err.println(" protocol_file_impl - protocol json file impl");
                System.err.println(" message_name       - message name ");
                return 1;
            }

            Optional<Integer> port = Optional.empty();
            Optional<String> protocol_file = Optional.empty();
            //Optional<String> protocol_file_impl = Optional.empty();
            Optional<String> message_name = Optional.empty();
            int arg = 0;
            List<Object> args = new ArrayList<>(arguments);


            if (args.contains("-port")) {
                arg = args.indexOf("-port") + 1;
                port = Optional.of(Integer.valueOf(args.get(arg).toString()));
                args.remove(arg);
                args.remove(arg - 1);
            }

            if (args.contains("-protocol_file")) {
                arg = args.indexOf("-protocol_file") + 1;
                protocol_file = Optional.of(args.get(arg).toString());
                args.remove(arg);
                args.remove(arg - 1);
            }

            /*
            if (args.contains("-protocol_file_impl")) {
                arg = args.indexOf("-protocol_file_impl") + 1;
                protocol_file_impl = Optional.of(args.get(arg).toString());
                args.remove(arg);
                args.remove(arg - 1);
            }
*/


            if (args.contains("-message_name")) {
                arg = args.indexOf("-message_name") + 1;
                message_name = Optional.of(args.get(arg).toString());
                args.remove(arg);
                args.remove(arg - 1);
            }

            String path = getPath(protocol_file.get());
            String fileName = getName(protocol_file.get());
            //String implPath = getPath(protocol_file_impl.get());
            //String implFileName = getName(protocol_file_impl.get());
            Class iface = org.softauto.core.Utils.getClazz(path,fileName);
            //Class implIface = org.softauto.core.Utils.getClazz(implPath,implFileName);
            Protocol protocol = SoftautoGrpcUtils.getProtocol(iface);
            Protocol.Message message = protocol.getMessages().get(message_name.get());
            if (message == null) {
                err.println(String.format("No message named '%s' found in protocol '%s'.", message_name.get(), protocol));
                return 1;
            }

            server = ServerBuilder.forPort(port.get())
                    .addService(SoftautoGrpcServer.createServiceDefinition(iface))
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
