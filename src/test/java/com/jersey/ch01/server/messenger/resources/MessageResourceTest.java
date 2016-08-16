package com.jersey.ch01.server.messenger.resources;

import com.jersey.ch01.server.messenger.model.Message;
import org.glassfish.jersey.message.internal.MessageBodyProviderNotFoundException;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;
import static org.junit.Assert.*;


public class MessageResourceTest extends AbstractTest{

    // for return type List, Map, Set, you have to use GenericType wrapper in order to use parser
    @Test
    public void testGetMessages() throws Exception {
        List<Message> messages = webTarget.path("messages").request().get(new GenericType<List<Message>>() {});

        System.out.println(messages);
        assertNotNull(messages);

    }

    @Test
    public void testGetMessage() throws Exception {
        Message message = webTarget.path("messages" + "/1").request(MediaType.APPLICATION_JSON).get(Message.class);
        System.out.println(message.toString());
        assertNotNull(message);
    }

    @Test(expected = NotFoundException.class)
    public void testGetMessageWithError() throws Exception {

        Message message = webTarget.path("messages" + "/a").request(MediaType.APPLICATION_JSON)
                .get(Message.class);
        System.out.println(message.toString());
        assertNotNull(message);
    }

    @Test
    public void testGetMessagesBy() throws Exception {
        URI uri = UriBuilder.fromUri(URL).path("messages/by").queryParam("year", 2015).build();

        WebTarget webTarget = client.target(uri);
        List<Message> messages =webTarget.request().get(new GenericType<List<Message>>() {});

        System.out.println(messages);

        assertNotNull(messages);
    }

    @Test
    public void testAddMessage() throws Exception {

        Message message = new Message(11L,"testing add","Bryan");

        Response response = webTarget.path("messages").request()
                .post(Entity.entity(message,MediaType.APPLICATION_JSON));

        assertTrue(201 == response.getStatus());
    }

    @Test
    public void testUpdateMessage() throws Exception {
        Message message = new Message(11L,"testing add","Bryan");
        long id = 100L;

        Response response = webTarget.path("messages/" + id).request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(message,MediaType.APPLICATION_JSON));

        assertTrue(200 == response.getStatus());

        assertNotNull(response.readEntity(Message.class));
    }

    @Test
    public void testDeleteMessage() throws Exception {
        long id = 100L;
        Response response = webTarget.path("messages/" + id).request(MediaType.APPLICATION_JSON)
                .delete();

        assertTrue(200 == response.getStatus());

        String result = response.readEntity(String.class);

        System.out.println(result);

        assertNotNull(result);
    }

    @Test
    public void testGetCommentResource() throws Exception {

    }
}