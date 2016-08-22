package com.jersey.examples.ch02.books.async.filters;

import javax.ws.rs.container.*;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;

//@PoweredBy
@Provider
public class PoweredByContainerResponseFilter implements ContainerResponseFilter{

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) {
        for (Annotation a : responseContext.getEntityAnnotations()) {
            if (a.annotationType() == PoweredBy.class) {
                String value = ((PoweredBy) a).value();
                responseContext.getHeaders().add("X-Powered-By", value);
            }
        }
    }

}