package com.jersey.examples.ch01.messenger.server.resources;


import com.jersey.examples.ch01.messenger.server.model.Comment;
import com.jersey.examples.ch01.messenger.server.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component("commentResource")
public class CommentResource {

    public CommentResource() {}

    @Autowired
    CommentService commentService;


    @GET
    public List<Comment> getAllComments(@PathParam("messageId") long messageId) {

        return commentService.getAllComments(messageId);
    }

    @POST
    public Comment addMessage(@PathParam("messageId") long messageId, Comment comment) {
        return commentService.addComment(messageId, comment);
    }

    @PUT
    @Path("/{commentId}")
    public Comment updateMessage(@PathParam("messageId") long messageId, @PathParam("commentId") long commentId, Comment comment) {
        comment.setId(commentId);
        return commentService.updateComment(messageId, comment);
    }

    @DELETE
    @Path("/{commentId}")
    public void deleteComment(@PathParam("messageId") long messageId, @PathParam("commentId") long commentId) {
        commentService.removeComment(messageId, commentId);
    }

    @GET
    @Path("/{commentId}")
    public Comment testCommentId(@PathParam("messageId") long messageId,@PathParam("commentId") long commentId) {
       return commentService.getComment(messageId, commentId);
    }
}
