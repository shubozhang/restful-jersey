package com.jersey.examples.ch01.messenger.server.resources;

import com.jersey.examples.ch01.messenger.server.model.Message;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import java.util.List;
import static org.junit.Assert.*;

/*
* This test uses im-memory grizzly container to run unit testing, so you don't have to
* deploy the war file to tomcat.
* */
public class MessageResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig().packages("com.jersey.examples.ch01.messenger");
    }


    // for return type List, Map, Set, you have to use GenericType wrapper in order to use parser
    @Test
    public void testGetMessages() throws Exception {
        List<Message> messages = target("messages").request().get(new GenericType<List<Message>>() {});

        System.out.println(messages);
        assertNotNull(messages);

    }

    @Test
    public void testGetMessage() throws Exception {
        Message message = target("messages" + "/1").request(MediaType.APPLICATION_JSON).get(Message.class);
        System.out.println(message.toString());
        assertNotNull(message);
    }

    @Test(expected = NotFoundException.class)
    public void testGetMessageWithError() throws Exception {

        Message message = target("messages" + "/a").request(MediaType.APPLICATION_JSON)
                .get(Message.class);
        System.out.println(message.toString());
        assertNotNull(message);
    }

    @Test
    public void testGetMessagesBy() throws Exception {
        List<Message> messages = target("messages/by").queryParam("year", 2015)
                .request().get(new GenericType<List<Message>>() {});

        System.out.println(messages);

        assertNotNull(messages);
    }

    @Test
    public void testAddMessage() throws Exception {

        Message message = new Message(11L,"testing add","Bryan");

        Response response = target("messages").request()
                .post(Entity.entity(message,MediaType.APPLICATION_JSON));

        assertTrue(201 == response.getStatus());
    }

    @Test
    public void testUpdateMessage() throws Exception {
        Message message = new Message(11L,"testing add","Bryan");
        long id = 100L;

        Response response = target("messages/" + id).request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(message,MediaType.APPLICATION_JSON));

        assertTrue(200 == response.getStatus());

        assertNotNull(response.readEntity(Message.class));
    }

    @Test
    public void testDeleteMessage() throws Exception {
        long id = 100L;
        Response response = target("messages/" + id).request(MediaType.APPLICATION_JSON)
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