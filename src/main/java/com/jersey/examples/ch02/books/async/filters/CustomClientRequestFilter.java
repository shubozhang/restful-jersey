package com.jersey.examples.ch02.books.async.filters;

import com.google.common.net.HttpHeaders;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;

public class CustomClientRequestFilter implements ClientRequestFilter {
    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {

        // example
        /*if (!requestContext.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, authentication);
        }*/
    }
}
