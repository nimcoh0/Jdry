package org.softauto.avro.tools;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.apache.avro.Protocol;
import org.apache.avro.generic.GenericData;
import org.softauto.core.CallFuture;
import org.softauto.grpc.SoftautoGrpcUtils;
import org.softauto.plugin.ProviderManager;
import org.softauto.plugin.api.Provider;
//import org.apache.avro.tool.Tool;


import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.*;

public class SendTool implements Tool {

    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(SendTool.class);

    @Override
    public int run(InputStream in, PrintStream out, PrintStream err, List<Object> arguments) throws Exception {
        try {

            if (arguments.size() >= 10) {
                System.err.println(
                        "Usage: -host <host> -port <port> [-protocol protocol] -protocol_file <protocol file> -message_name <message name> (-data d | -file f)");
                System.err.println(" host           - receiver host ");
                System.err.println(" port           - receiver port ");
                System.err.println(" protocol       - any transceiver define in the classpath . default is GRPC ");
                System.err.println(" protocol_file  - protocol json file ");
                System.err.println(" message_name   - message name ");
                System.err.println(" data           - JSON-encoded request parameters. ");
                System.err.println(" file           - Data file containing request parameters. ");
                return 1;
            }

            Optional<String> host = Optional.empty();
            Optional<Integer> port = Optional.empty();
            Optional<String> transceiver = Optional.empty();
            Optional<String> protocol_file = Optional.empty();
            Optional<String> message_name = Optional.empty();
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

            if (args.contains("-protocol")) {
                arg = args.indexOf("-protocol") + 1;
                transceiver = Optional.of(args.get(arg).toString());
                args.remove(arg);
                args.remove(arg - 1);
            }else{
                transceiver = Optional.of("RPC");
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

            CallFuture<?> callFuture = new CallFuture<>();
            Field[] fs =  ((GenericData.Record)datum).getClass().getDeclaredFields();
            fs[1].setAccessible(true);
            Object[] o = ((Object[])fs[1].get(datum));
            Provider provider = ProviderManager.provider(transceiver.get()).create().iface(iface);
            provider.exec(message_name.get(),o,callFuture,channel);

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
