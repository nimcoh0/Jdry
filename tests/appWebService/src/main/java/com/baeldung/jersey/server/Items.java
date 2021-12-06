package com.baeldung.jersey.server;

import org.softauto.annotations.ExposedForTesting;
import org.softauto.jaxrs.annotations.JAXRS;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;

@Path("items")
public class Items {

    @JAXRS
    @ExposedForTesting
    @GET
    @Path("/cookie")
    public String readCookieParam(@CookieParam("cookieParamToRead") String cookieParamToRead) {
        return "Cookie parameter value is [" + cookieParamToRead + "]";
    }

    @JAXRS
    @ExposedForTesting
    @GET
    @Path("/header")
    public String readHeaderParam(@HeaderParam("headerParamToRead") String headerParamToRead) {
        return "Header parameter value is [" + headerParamToRead + "]";
    }

    @PermitAll
    @JAXRS
    @ExposedForTesting
    @GET
    @Path("/path/{pathParamToRead}")
    public String readPathParam(@PathParam("pathParamToRead") String pathParamToRead) {
        return "Path parameter value is [" + pathParamToRead + "]";
    }

    @JAXRS
    @ExposedForTesting
    @GET
    @Path("/query")
    public String readQueryParam(@QueryParam("queryParamToRead") String queryParamToRead) {
        return "Query parameter value is [" + queryParamToRead + "]";
    }

    @JAXRS
    @ExposedForTesting
    @POST
    @Path("/form")
    public String readFormParam(@FormParam("formParamToRead") String formParamToRead) {
        return "Form parameter value is [" + formParamToRead + "]";
    }

    @JAXRS
    @ExposedForTesting
    @GET
    @Path("/matrix")
    public String readMatrixParam(@MatrixParam("matrixParamToRead") String matrixParamToRead) {
        return "Matrix parameter value is [" + matrixParamToRead + "]";
    }

    @JAXRS
    @ExposedForTesting
    @POST
    @Path("/bean/{pathParam}")
    public String readBeanParam(@BeanParam ItemParam itemParam) {
        return itemParam.toString();
    }
}