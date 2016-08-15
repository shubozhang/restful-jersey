package com.jersey.ch01.server.messenger.exception;


import com.jersey.ch01.server.messenger.model.ErrorMessage;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/*
* This generic exception mapper will catch all exception
* */
// This class intentionally doesn't have the @Provider annotation.
// It has been disabled in order to try out other ways of throwing exceptions in JAX-RS

//@Provider
 public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(), 500, "http://testing.com");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMessage)
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
