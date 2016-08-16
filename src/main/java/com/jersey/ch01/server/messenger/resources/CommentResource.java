package com.jersey.ch01.server.messenger.resources;


import com.jersey.ch01.server.messenger.model.Comment;
import com.jersey.ch01.server.messenger.service.CommentService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommentResource {

    private CommentService commentService = new CommentService();


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
