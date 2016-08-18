package com.jersey;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fasterxml.jackson.jaxrs.xml.JacksonXMLProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.HttpMethodOverrideFilter;
import org.glassfish.jersey.server.filter.UriConnegFilter;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;


/*
* It seems that register (json/xml) does not work, although MyApplication is invoked.
* Date is always generated in timestamp format.
*
* com.jersey.examples.ch02.books.ObjectMapperProvider.class solves this date format issue.
* */

public class MyApplication extends ResourceConfig {

    public MyApplication() {
        JacksonJsonProvider json = new JacksonJsonProvider()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);

        JacksonXMLProvider xml = new JacksonXMLProvider()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.INDENT_OUTPUT, true);

        HashMap<String,MediaType> mappings = new HashMap<String,MediaType>();
        mappings.put("xml",MediaType.APPLICATION_XML_TYPE);
        mappings.put("json", MediaType.APPLICATION_JSON_TYPE);
        UriConnegFilter uriConnegFilter = new UriConnegFilter(mappings, null);

        register(json);
        register(xml);
        register(JacksonFeatures.class);
        packages("com.jersey.examples");
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true); // Send the customized error back to client
        register(HttpMethodOverrideFilter.class); // Test filter
        register(uriConnegFilter);
    }
}
