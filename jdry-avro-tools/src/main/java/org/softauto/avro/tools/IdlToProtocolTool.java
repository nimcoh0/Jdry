package org.softauto.avro.tools;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

public class IdlToProtocolTool implements Tool{


    @Override
    public int run(InputStream in, PrintStream out, PrintStream err, List<Object> arguments) throws Exception {
        return 0;
    }

    @Override
    public String getName() {
        return "idl2protocol";
    }

    @Override
    public String getShortDescription() {
        return "Extract JSON protocol from an Jdry IDL file";
    }
}
