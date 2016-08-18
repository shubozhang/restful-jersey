package com.jersey.examples.ch02.books;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Component;


/*
* To config that serialization wraps date in date format, not timestamp.
* */

// tell spring to look for this.
@Component
// tell spring it's a provider (type is determined by the implements)
@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {
    @Override
    public ObjectMapper getContext(Class<?> type) {
        // create the objectMapper.
        ObjectMapper objectMapper = new ObjectMapper();
        // configure the object mapper here, eg.
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }
}
