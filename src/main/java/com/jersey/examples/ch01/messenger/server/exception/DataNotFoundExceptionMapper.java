package com.jersey.examples.ch01.messenger.server.exception;



import com.jersey.examples.ch01.messenger.server.model.ErrorMessage;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException>{
    @Override
    public Response toResponse(DataNotFoundException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(), 404, "http://testing.com");
        return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
    }
}
