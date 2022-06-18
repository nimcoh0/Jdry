package org.softauto.core.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import javax.ws.rs.core.Response;
import java.io.IOException;

public class WsRsResponsSerializer extends StdSerializer<Response> {

    public WsRsResponsSerializer() {
        this(null);
    }

    public WsRsResponsSerializer(Class<Response> t) {
        super(t);
    }

    @Override
    public void serialize(Response response, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        Object entity = response.getEntity();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("entity", entity);
        jsonGenerator.writeEndObject();
    }


}
