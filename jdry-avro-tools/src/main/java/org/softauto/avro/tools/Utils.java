package org.softauto.avro.tools;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.io.DecoderFactory;

import java.io.File;
import java.io.IOException;

public class Utils {

    static Object jsonToGenericDatum(Schema schema, String jsonData) throws IOException {
        GenericDatumReader<Object> reader = new GenericDatumReader<>(schema);
        Object datum = reader.read(null, DecoderFactory.get().jsonDecoder(schema, jsonData));
        return datum;
    }

    static Object datumFromFile(Schema schema, String file) throws IOException {
        try (DataFileReader<Object> in = new DataFileReader<>(new File(file), new GenericDatumReader<>(schema))) {
            return in.next();
        }
    }
}
