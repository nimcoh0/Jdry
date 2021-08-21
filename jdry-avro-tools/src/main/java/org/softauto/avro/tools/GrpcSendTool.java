package org.softauto.avro.tools;

import io.grpc.CallOptions;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.apache.avro.Protocol;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.io.JsonEncoder;
import org.softauto.core.CallFuture;
import org.softauto.core.CallbackToResponseStreamObserverAdpater;
import org.softauto.grpc.ServiceDescriptor;
import org.softauto.grpc.SoftautoGrpcUtils;
//import org.apache.avro.tool.Tool;


import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.*;

public class GrpcSendTool implements Tool {

    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(GrpcSendTool.class);

    @Override
    public int run(InputStream in, PrintStream out, PrintStream err, List<Object> arguments) throws Exception {
        try {

            if (arguments.size() != 10) {
                System.err.println(
                        "Usage: -host <host> -port <port>  -protocol_file <protocol file> -message_name <message name> (-data d | -file f)");
                System.err.println(" host           - receiver host ");
                System.err.println(" port           - receiver port ");
                System.err.println(" protocol_file  - protocol json file ");
                System.err.println(" message_name   - message name ");
                System.err.println(" data           - JSON-encoded request parameters. ");
                System.err.println(" file           - Data file containing request parameters. ");
                return 1;
            }

            Optional<String> host = Optional.empty();
            Optional<Integer> port = Optional.empty();
            Optional<String> protocol_file = Optional.empty();
            Optional<String> message_name = Optional.empty();
            //Optional<String> args = Optional.empty();
            int arg = 0;
            List<Object> args = new ArrayList<>(arguments);




            if (args.contains("-host")) {
                arg = arguments.indexOf("-host") + 1;
                host = Optional.of(args.get(arg).toString());
                args.remove(arg);
                args.remove(arg - 1);
            }

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

            if (args.contains("-message_name")) {
                arg = args.indexOf("-message_name") + 1;
                message_name = Optional.of(args.get(arg).toString());
                args.remove(arg);
                args.remove(arg - 1);
            }


            OptionParser p = new OptionParser();
            OptionSpec<String> file = p.accepts("file", "Data file containing request parameters.").withRequiredArg()
                    .ofType(String.class);
            OptionSpec<String> data = p.accepts("data", "JSON-encoded request parameters.").withRequiredArg()
                    .ofType(String.class);
            OptionSet opts = p.parse(args.toArray(new String[0]));
            List<Object> _args = (List<Object>) opts.nonOptionArguments();



            ManagedChannel channel = ManagedChannelBuilder.forAddress(host.get(), port.get()).usePlaintext().build();
            String path = protocol_file.get().substring(0,protocol_file.get().lastIndexOf("/"));
            String fileName = protocol_file.get().substring(protocol_file.get().lastIndexOf("/")+1);
            Class iface = org.softauto.core.Utils.getClazz(path,fileName);
            Protocol protocol = SoftautoGrpcUtils.getProtocol(iface);//Protocol.parse(new File(protocol_file.get()));
            Protocol.Message message = protocol.getMessages().get(message_name.get());
            if (message == null) {
                err.println(String.format("No message named '%s' found in protocol '%s'.", message_name.get(), protocol));
                return 1;
            }


            Object datum;
            if (data.value(opts) != null) {
                datum = Utils.jsonToGenericDatum(message.getRequest(), data.value(opts));
            } else if (file.value(opts) != null) {
                datum = Utils.datumFromFile(message.getRequest(), file.value(opts));
            } else {
                err.println("One of -data or -file must be specified.");
                return 1;
            }

            MethodDescriptor<Object[], Object> m = ServiceDescriptor.create(iface).getMethod(message_name.get(), MethodDescriptor.MethodType.UNARY);
            CallFuture<?> callFuture = new CallFuture<>();
            StreamObserver<Object> observerAdpater = new CallbackToResponseStreamObserverAdpater<>(callFuture, channel);
            Field[] fs =  ((GenericData.Record)datum).getClass().getDeclaredFields();
            fs[1].setAccessible(true);
            Object[] o = ((Object[])fs[1].get(datum));
            ClientCalls.asyncUnaryCall(channel.newCall(m, CallOptions.DEFAULT), o, observerAdpater);
            Object response =  callFuture.get();
            dump(out,  response);
            logger.info("successfully send message ");
        }catch (Exception e){
            logger.error("fail send message ", e);
            return 1;
        }
        return 0;

    }

    private void dump(PrintStream out,  Object datum) throws IOException {
        out.print(datum);
        out.flush();
    }


    @Override
    public String getName() {
        return "grpcsend";
    }

    @Override
    public String getShortDescription() {
        return "Sends a single GRPC message.";
    }
}
