package com.jersey.examples.ch02.books.async.filters;

import javax.annotation.Priority;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@Provider
@Priority(Priorities.AUTHENTICATION)
public class CustomContainerRequestFilter implements ContainerRequestFilter {

    public static final String HEADER_NAME = "X-Requested-By";
    private static final Set<String> METHODS_TO_IGNORE;

    static {
        HashSet<String> mti = new HashSet<>();
        mti.add("GET");
        mti.add("OPTIONS");
        mti.add("HEAD");
        METHODS_TO_IGNORE = Collections.unmodifiableSet(mti);
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // examples
       /* if (!METHODS_TO_IGNORE.contains(requestContext.getMethod()) && !requestContext.getHeaders().containsKey(HEADER_NAME)) {
            throw new BadRequestException();
        }*/
    }
}
