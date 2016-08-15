package com.jersey.ch01.server.messenger.database;


import com.jersey.ch01.server.messenger.model.Message;
import com.jersey.ch01.server.messenger.model.Profile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shubo on 5/11/2015.
 */
public class DatabaseClass {

    private static Map<Long, Message> messages = new HashMap();
    private static Map<String, Profile> profiles = new HashMap();

    public static Map<Long, Message> getMessages() {
        return messages;
    }

    public static Map<String, Profile> getProfiles() {
        return profiles;
    }
}
