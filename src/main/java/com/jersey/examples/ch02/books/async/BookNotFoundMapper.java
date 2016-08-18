package com.jersey.examples.ch02.books.async;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by shubo.zhang on 8/18/2016.
 */
@Provider
public class BookNotFoundMapper implements ExceptionMapper<BookNotFoundException> {

    public Response toResponse(BookNotFoundException ex) {
        return Response.status(404).entity(ex.getMessage()).type("text/plain").build();
    }
}