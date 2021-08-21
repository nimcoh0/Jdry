package org.softauto.avro.tools;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

public interface Tool {
    int run(InputStream in, PrintStream out, PrintStream err,List<Object> arguments) throws Exception;

    String getName();

    String getShortDescription();
}
