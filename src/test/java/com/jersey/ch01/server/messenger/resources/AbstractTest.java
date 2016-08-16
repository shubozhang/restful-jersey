package com.jersey.ch01.server.messenger.resources;


import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.net.URI;

public abstract class AbstractTest {

    protected static final String URL = "http://localhost:48080/webapi/";
    protected Client client;
    protected WebTarget webTarget;

    @Before
    public void setup() {
        this.client = ClientBuilder.newClient();
        this.webTarget = this.client.target(URL);
    }

}
