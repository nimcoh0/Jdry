package org.softauto.avro.tools;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.softauto.serializer.Serializer;
import org.softauto.serializer.service.Message;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

public class GrpcSendTool implements Tool {

    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(GrpcSendTool.class);

    @Override
    public int run(InputStream in, PrintStream out, PrintStream err, List<Object> arguments) throws Exception {
        try {

            if (arguments.size() >= 10) {
                System.err.println(
                        "Usage: -host <host> -port <port>  (-data d | -file f)");
                System.err.println(" host           - receiver host ");
                System.err.println(" port           - receiver port ");
                System.err.println(" data           - JSON-encoded request parameters. ");
                System.err.println(" file           - Data file containing request parameters. ");
                return 1;
            }

            Optional<String> host = Optional.empty();
            Optional<Integer> port = Optional.empty();
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

            OptionParser p = new OptionParser();
            OptionSpec<String> file = p.accepts("file", "Data file containing request parameters.").withRequiredArg()
                    .ofType(String.class);
            OptionSpec<String> data = p.accepts("data", "JSON-encoded request parameters.").withRequiredArg()
                    .ofType(String.class);
            OptionSet opts = p.parse(args.toArray(new String[0]));
            List<Object> _args = (List<Object>) opts.nonOptionArguments();
            ManagedChannel channel = ManagedChannelBuilder.forAddress(host.get(), port.get()).usePlaintext().build();
            Serializer serializer = new Serializer().setChannel(channel);
            Message message = null;
            if (data.value(opts) != null) {
                message = Message.newBuilder().addData("data", data.value(opts)).build();
            } else if (file.value(opts) != null) {
                message = Message.newBuilder().addData("data", file.value(opts)).build();
            }else {
                err.println("One of -data or -file must be specified.");
                return 1;
            }
            Object response = serializer.write(message);
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
