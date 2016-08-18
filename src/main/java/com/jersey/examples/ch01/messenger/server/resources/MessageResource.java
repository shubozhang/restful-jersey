package com.jersey.examples.ch01.messenger.server.resources;

import com.jersey.examples.ch01.messenger.server.model.Message;
import com.jersey.examples.ch01.messenger.server.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Path("/messages")
@Produces({MediaType.APPLICATION_JSON})
@Consumes(MediaType.APPLICATION_JSON)
public class MessageResource {


    @Autowired
    MessageService messageService;

    @Autowired
    CommentResource commentResource;

    @GET
    public List<Message> getMessages() {
        return messageService.getAllMessages();
    }

    @GET
    @Path("/{messageId}")
    public Message getMessage(@PathParam("messageId") long messageId, @Context UriInfo uriInfo) {
        Message message = messageService.getMessage(messageId);
        message.addLink(getUriForSelf(message, uriInfo), "self");
        message.addLink(getUriForProfile(message, uriInfo), "profile");
        message.addLink(getUriForComments(message, uriInfo), "comments");
        return message;
    }



    @GET
    @Path("/by")
    public List<Message> getMessagesBy(@QueryParam("year") int year,
                                     @QueryParam("start") int start,
                                     @QueryParam("size") int size) {
        if (year > 0) {
            return messageService.getAllMessagesByYear(year);
        }
        if (start >= 0 && size > 0) {
            return messageService.getAllMessagePaginated(start, size);
        }
        return messageService.getAllMessages();
    }

// alternative option for QueryParam
   /* @GET
    @Path("/by")
    public List<Message> getMessagesBy(@BeanParam MessageFilterBean filterBean) {
        if (filterBean.getYear() > 0) {
            return messageService.getAllMessagesByYear(year);
        }
        if (filterBean.getStart() >= 0 && filterBean.getSize > 0) {
            return messageService.getAllMessagePaginated(filterBean.getStart(), filterBean.getSize());
        }
        return messageService.getAllMessages();
    }*/


    @POST
    public Response addMessage(Message message, @Context UriInfo uriInfo) throws URISyntaxException {

        Message newMessage = messageService.addMessage(message);
        String newId = String.valueOf(newMessage.getId());
        URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
        return Response.created(uri).entity(newMessage).build();
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

    /*
    * For sub resource, do not use a http method. Sub resource class will handle it.
    * */
    @Path("/{messageId}/comments")
    public CommentResource getCommentResource() {
        return this.commentResource;
    }

    private String getUriForSelf(Message message, UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder().path(MessageResource.class)
                .path(Long.toString(message.getId())).build().toString();
    }

    private String getUriForProfile(Message message, UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder().path(ProfileResource.class)
                .path(message.getAuthor()).build().toString();
    }

    private String getUriForComments(Message message, UriInfo uriInfo) {
        URI uri =  uriInfo.getBaseUriBuilder().
                        path(MessageResource.class).
                        path(MessageResource.class, "getCommentResource").
                        path(CommentResource.class).
                        resolveTemplate("messageId", message.getId()).
                        build();

        return uri.toString();
    }
}
