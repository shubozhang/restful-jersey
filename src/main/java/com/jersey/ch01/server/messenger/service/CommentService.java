package com.jersey.ch01.server.messenger.service;




import com.jersey.ch01.server.messenger.database.DatabaseClass;
import com.jersey.ch01.server.messenger.model.Comment;
import com.jersey.ch01.server.messenger.model.ErrorMessage;
import com.jersey.ch01.server.messenger.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.*;

@Repository("commentService")
public class CommentService {

    @Autowired
    DatabaseClass db;

    public CommentService(){
    }

    public List<Comment> getAllComments(long messageId) {
        Map<Long, Comment> comments = db.getMessages().get(messageId).getComments();
        return new ArrayList<Comment>(comments.values());
    }

    public Comment getComment(long messageId, long commentId) {
        ErrorMessage errorMessage = new ErrorMessage("Not Found", 404, "http://testing.com");
        Response response = Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
        Message message = db.getMessages().get(messageId);
        if (message == null) {
            throw new WebApplicationException(response);
        }
        Map<Long, Comment> comments = db.getMessages().get(messageId).getComments();
        Comment comment = comments.get(commentId);
        if (comment == null) {
            throw new WebApplicationException(response);
        }
        return comment;
    }

    public Comment addComment(long messageId, Comment comment) {
        Map<Long, Comment> comments = db.getMessages().get(messageId).getComments();
        comment.setId(comments.size() + 1);
        comments.put(comment.getId(), comment);
        return comment;
    }

    public Comment updateComment(long messageId, Comment comment) {
        Map<Long, Comment> comments = db.getMessages().get(messageId).getComments();
        if (comment.getId() <= 0) {
            return null;
        }
        comments.put(comment.getId(), comment);
        return comment;
    }

    public Comment removeComment(long messageId, long commentId) {
        Map<Long, Comment> comments = db.getMessages().get(messageId).getComments();
        return comments.remove(commentId);
    }
}
