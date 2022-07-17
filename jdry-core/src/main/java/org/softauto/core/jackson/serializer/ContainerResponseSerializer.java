package org.softauto.core.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ContainerResponse;

import java.io.IOException;

public class ContainerResponseSerializer extends StdSerializer<ContainerResponse> {

    public ContainerResponseSerializer() {
        this(null);
    }

    public ContainerResponseSerializer(Class<ContainerResponse> t) {
        super(t);
    }


    @Override
    public void serialize(ContainerResponse containerResponse, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        int statue = containerResponse.getStatus();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("statue", statue);
        jsonGenerator.writeEndObject();
    }
}
