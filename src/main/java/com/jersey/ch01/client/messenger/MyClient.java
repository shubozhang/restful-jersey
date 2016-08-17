package com.jersey.ch01.client.messenger;

import com.jersey.ch01.server.messenger.model.Message;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.List;


/*
* Server should be deployed in Tomcat
* */
public class MyClient {

    public static void main(String[] args) {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:48080/webapi/");

        List<Message> messages = webTarget.path("messages").request().get(new GenericType<List<Message>>() {});

        System.out.println(messages);
    }
}
