package com.jersey.ch01.server.messenger.database;


import com.jersey.ch01.server.messenger.model.Comment;
import com.jersey.ch01.server.messenger.model.Message;
import com.jersey.ch01.server.messenger.model.Profile;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shubo on 5/11/2015.
 */

@Repository("databaseClass")
public class DatabaseClass {

    private Map<Long, Message> messages = new HashMap();
    private Map<String, Profile> profiles = new HashMap();

    public DatabaseClass() {

        Message m1 = new Message(1L, "message 1", "Abe");
        Message m2 = new Message(2L, "message 2", "Bob");
        Message m3 = new Message(3L, "message 3", "Cook");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        try {
            date = sdf.parse("21/12/2015");
            m2.setCreated(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        messages.put(1L, m1);
        messages.put(2L, m2);
        messages.put(3L, m3);


        Comment comment1 = new Comment(1L,"testing comment", new Date(),"Bryan");
        Map<Long, Comment> comments = new HashMap();
        comments.put(1L,comment1);
        messages.get(1L).setComments(comments);


        profiles.put("bryan", new Profile(1L,"bryan", "shubo", "zhang"));
    }

    public  Map<Long, Message> getMessages() {
        return messages;
    }

    public  Map<String, Profile> getProfiles() {
        return profiles;
    }

}
