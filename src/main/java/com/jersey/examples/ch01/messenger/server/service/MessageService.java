package com.jersey.examples.ch01.messenger.server.service;

import com.jersey.examples.ch01.messenger.server.exception.DataNotFoundException;
import com.jersey.examples.ch01.messenger.server.model.Message;
import com.jersey.examples.ch01.messenger.server.database.DatabaseClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by Shubo on 5/10/2015.
 */

@Repository("messageService")
public class MessageService {


    @Autowired
    DatabaseClass db;

    public MessageService() {

    }

    public List<Message> getAllMessages() {
        return new ArrayList<Message>(db.getMessages().values());
    }

    public List<Message> getAllMessagesByYear(int year) {
        List<Message> messagesByYear = new ArrayList();
        for (Message message : db.getMessages().values()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(message.getCreated());
            if (cal.get(Calendar.YEAR) == year) {
                messagesByYear.add(message);
            }
        }
        return messagesByYear;
    }

    public List<Message> getAllMessagePaginated(int start, int size) {
        ArrayList<Message> list = new ArrayList<Message>(db.getMessages().values());
        if (start + size > list.size()) {
            return new ArrayList<Message>();
        }
        return list.subList(start, start + size);
    }

    public Message getMessage(long id) {
        Message message = db.getMessages().get(id);
        if (message == null) {
            throw new DataNotFoundException("Message with id " + id + " not found");
        }
        return message;
    }

    public Message addMessage(Message message) {
        message.setId(db.getMessages().size() + 1);
        db.getMessages().put(message.getId(), message);
        return message;
    }

    public Message updateMessage(Message message) {
        if (message.getId() <= 0) {
            return null;
        }
        db.getMessages().put(message.getId(), message);
        return message;

    }

    public String deleteMessage(long id) {
        db.getMessages().remove(id);
        return "message " + id + " is deleted !";
    }


}
