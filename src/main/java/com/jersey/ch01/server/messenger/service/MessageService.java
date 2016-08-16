package com.jersey.ch01.server.messenger.service;

import com.jersey.ch01.server.messenger.database.DatabaseClass;
import com.jersey.ch01.server.messenger.exception.DataNotFoundException;
import com.jersey.ch01.server.messenger.model.Message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Shubo on 5/10/2015.
 */
public class MessageService {


    private Map<Long, Message> messages = DatabaseClass.getMessages();

    public MessageService() {
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

    }

    public List<Message> getAllMessages() {
        return new ArrayList<Message>(messages.values());
    }

    public List<Message> getAllMessagesByYear(int year) {
        List<Message> messagesByYear = new ArrayList();
        for (Message message : messages.values()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(message.getCreated());
            if (cal.get(Calendar.YEAR) == year) {
                messagesByYear.add(message);
            }
        }
        return messagesByYear;
    }

    public List<Message> getAllMessagePaginated(int start, int size) {
        ArrayList<Message> list = new ArrayList<Message>(messages.values());
        if (start + size > list.size()) {
            return new ArrayList<Message>();
        }
        return list.subList(start, start + size);
    }

    public Message getMessage(long id) {
        Message message = messages.get(id);
        if (message == null) {
            throw new DataNotFoundException("Message with id " + id + " not found");
        }
        return message;
    }

    public Message addMessage(Message message) {
        message.setId(messages.size() + 1);
        messages.put(message.getId(), message);
        return message;
    }

    public Message updateMessage(Message message) {
        if (message.getId() <= 0) {
            return null;
        }
        messages.put(message.getId(), message);
        return message;

    }

    public String deleteMessage(long id) {
        messages.remove(id);
        return "message " + id + " is deleted !";
    }


}
