package com.jersey.ch02.client.messenger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;


public class MyClient {

    public static void main(String[] args) {
        /*Client client = ClientBuilder.newClient();

        WebTarget webTarget = client.target("http://localhost:48080/webapi/");


        String result = webTarget.path("messages").request().get(String.class);
        System.out.println(result);*/
    }
}
