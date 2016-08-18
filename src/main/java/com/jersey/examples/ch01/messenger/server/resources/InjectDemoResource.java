package com.jersey.examples.ch01.messenger.server.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 * Created by Shubo on 5/12/2015.
 */

@Path("/injectdemo")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_PLAIN)
public class InjectDemoResource {

    @GET
    @Path("annotations")
    public String getParamsUsingAnnotations(@MatrixParam("param") String matrixParam,
                                            @HeaderParam("headerValue") String header,
                                            @CookieParam("cookieName") String cookieName ) {

        return "Matrix param: " + matrixParam + " and Header: " + header + " and cookieName: " + cookieName;
    }


    @GET
    @Path("context")
    public String getParamsUsingContext(@Context UriInfo uriInfo, @Context HttpHeaders headers) {

        String path = uriInfo.getAbsolutePath().toString();
        String cookie = headers.getCookies().toString();
        return "path: " + path + " and cookie: " + cookie;
    }
}
