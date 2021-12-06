package com.baeldung.jersey.server;

import com.baeldung.jersey.server.model.Person;
import org.softauto.annotations.ExposedForTesting;
import org.softauto.jaxrs.annotations.JAXRS;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/response")
public class Responder {

    @JAXRS
    @ExposedForTesting
    @GET
    @Path("/ok")
    public Response getOkResponse() {

        String message = "This is a text response";

        return Response
          .status(Response.Status.OK)
          .entity(message)
          .build();
    }

    @JAXRS
    @ExposedForTesting
    @GET
    @Path("/not_ok")
    public Response getNOkTextResponse() {

        String message = "There was an internal server error";

        return Response
          .status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(message)
          .build();
    }

    @JAXRS
    @ExposedForTesting
    @GET
    @Path("/text_plain")
    public Response getTextResponseTypeDefined() {

        String message = "This is a plain text response";

        return Response
          .status(Response.Status.OK)
          .entity(message)
          .type(MediaType.TEXT_PLAIN)
          .build();
    }

    @JAXRS
    @ExposedForTesting
    @GET
    @Path("/text_plain_annotation")
    @Produces({ MediaType.TEXT_PLAIN })
    public Response getTextResponseTypeAnnotated() {

        String message = "This is a plain text response via annotation";

        return Response
          .status(Response.Status.OK)
          .entity(message)
          .build();
    }

    @JAXRS
    @ExposedForTesting
    @GET
    @Path("/pojo")
    public Response getPojoResponse() {

        Person person = new Person("Abh", "Nepal");

        return Response
          .status(Response.Status.OK)
          .entity(person)
          .build();
    }

    @JAXRS
    @ExposedForTesting
    @GET
    @Path("/json")
    public Response getJsonResponse() {

        String message = "{\"hello\": \"This is a JSON response\"}";

        return Response
          .status(Response.Status.OK)
          .entity(message)
          .type(MediaType.APPLICATION_JSON)
          .build();
    }

    @JAXRS
    @ExposedForTesting
    @GET
    @Path("/xml")
    @Produces(MediaType.TEXT_XML)
    public String sayXMLHello() {
        return "<?xml version=\"1.0\"?>" + "<hello> This is a xml response </hello>";
    }

    @JAXRS
    @ExposedForTesting
    @GET
    @Path("/html")
    @Produces(MediaType.TEXT_HTML)
    public String sayHtmlHello() {
        return "<html> " + "<title>" + " This is a html title  </title>" + "<body><h1>" + " This is a html response body " + "</body></h1>" + "</html> ";
    }
}