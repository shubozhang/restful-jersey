package com.jersey;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fasterxml.jackson.jaxrs.xml.JacksonXMLProvider;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by shubo.zhang on 8/17/2016.
 */
public class MyApplication extends ResourceConfig {

    public MyApplication() {
        JacksonJsonProvider json = new JacksonJsonProvider().
                configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).
                configure(SerializationFeature.INDENT_OUTPUT, true);

        JacksonXMLProvider xml = new JacksonXMLProvider().
                configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).
                configure(SerializationFeature.INDENT_OUTPUT, true);
        register(json);
        register(xml);
        register(JacksonFeatures.class);
        packages("com.jersey");
    }
}
