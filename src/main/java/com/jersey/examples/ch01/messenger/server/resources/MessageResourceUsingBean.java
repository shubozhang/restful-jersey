package com.jersey.examples.ch01.messenger.server.resources;

import com.jersey.examples.ch01.messenger.server.model.Message;
import com.jersey.examples.ch01.messenger.server.service.MessageService;
import com.jersey.examples.ch01.messenger.server.resources.beans.MessageFilterBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Shubo on 5/12/2015.
 */
@Path("/messagesUsingBean")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MessageResourceUsingBean {


    @Autowired
    MessageService messageService;


    @GET
    public List<Message> getMessages(@BeanParam MessageFilterBean filterBean) {
        if (filterBean.getYear() > 0) {
            return messageService.getAllMessagesByYear(filterBean.getYear());
        }
        if (filterBean.getStart() >= 0 && filterBean.getSize() > 0) {
            return messageService.getAllMessagePaginated(filterBean.getStart(), filterBean.getSize());
        }
        return messageService.getAllMessages();
    }

    @GET
    @Path("/{messageId}")
    public Message getMessage(@PathParam("messageId") long messageId) {
        return messageService.getMessage(messageId);
    }


    @POST
    public Message addMessage(Message message) {

        return messageService.addMessage(message);
    }

    @PUT
    @Path("/{messageId}")
    public Message updateMessage(@PathParam("messageId") long messageId, Message message) {
        message.setId(messageId);
        return messageService.updateMessage(message);
    }

    @DELETE
    @Path("/{messageId}")
    public String deleteMessage(@PathParam("messageId") long messageId) {
        return messageService.deleteMessage(messageId);
    }
}
