package org.softauto.core.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.apache.commons.codec.binary.Base64;
//import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainer;
//import org.glassfish.jersey.oauth1.signature.Base64;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class ContainerRequestSerializer extends StdSerializer<ContainerRequest> {

    public ContainerRequestSerializer() {
        this(null);
    }

    public ContainerRequestSerializer(Class<ContainerRequest> t) {
        super(t);
    }

    @Override
    public void serialize(ContainerRequest requestContext, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String username = null;
        String password = null;
        MultivaluedMap<String, String> headers = ((ContainerRequestContext)requestContext).getHeaders();
        //((GrizzlyHttpContainer)requestContext.getSecurityContext()).getRequestExecutorProvider();

        //InputStream inputStream = connection.getInputStream();
        //if(requestContext.hasEntity()){
            //InputStream inputStream =  requestContext.getEntityStream();
        //}
        //String path = requestContext.getAbsolutePath().getPath();
        String method = requestContext.getMethod();
        String path = requestContext.getRequestUri().toString();
        String media = requestContext.getMediaType().toString();
        //int statue = requestContext.getAbortResponse().getStatusInfo().getStatusCode();
        //Set<String>  allowedMethods  = requestContext.getAbortResponse().getAllowedMethods();
        //String allowedMethods =  Arrays.toString(requestContext.getAbortResponse().getAllowedMethods().toArray());
        List<String> authorization = headers.get("Authorization");
        if(authorization != null && !authorization.isEmpty()) {
            String encodedUserPassword = authorization.get(0).replaceFirst("Basic" + " ", "");
            Base64 base64 = new Base64();
            //String usernameAndPassword = new String(base64.decode(encodedUserPassword.getBytes()));
            String usernameAndPassword = new String(base64.decode(encodedUserPassword.getBytes()));
            //String usernameAndPassword = new String(Base64.decode(String.valueOf(encodedUserPassword.getBytes())));
            StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
            username = tokenizer.nextToken();
            password = tokenizer.nextToken();
        }
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("path", path);
        jsonGenerator.writeObjectField("method", method);
        jsonGenerator.writeObjectField("media", media);
        //jsonGenerator.writeObjectField("statue", statue);
        //jsonGenerator.writeObjectField("allowedMethods", allowedMethods);
        if(authorization != null && !authorization.isEmpty()) {
            jsonGenerator.writeObjectField("Authentication", "Basic");
            //jsonGenerator.writeObjectField("Authorization", Arrays.toString(authorization.toArray(new String[authorization.size()])));
            jsonGenerator.writeObjectField("username", username);
            jsonGenerator.writeObjectField("password", password);

        }else {
            jsonGenerator.writeObjectField("Authorization", "PermitAll");
        }
        jsonGenerator.writeEndObject();
    }
}
