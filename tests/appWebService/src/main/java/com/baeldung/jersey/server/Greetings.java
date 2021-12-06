package com.baeldung.jersey.server;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.baeldung.jersey.server.config.HelloBinding;
import org.softauto.annotations.ExposedForTesting;
import org.softauto.jaxrs.annotations.JAXRS;

@Path("/greetings")
public class Greetings {

    @PermitAll
    @JAXRS
    @ExposedForTesting
    @GET
    @HelloBinding
    public String getHelloGreeting() {
        return "hello";
    }

    @JAXRS
    @ExposedForTesting
    @GET
    @Path("/hi")
    public String getHiGreeting() {
        return "hi";
    }

    @JAXRS
    @ExposedForTesting
    @POST
    @Path("/custom")
    public Response getCustomGreeting(String name) {
        return Response.status(Status.OK.getStatusCode())
            .build();
    }
}
