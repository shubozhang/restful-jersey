package com.jersey.examples.ch01.messenger.server.service;



import com.jersey.examples.ch01.messenger.server.database.DatabaseClass;
import com.jersey.examples.ch01.messenger.server.model.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubo on 5/12/2015.
 */
@Repository("profileService")
public class ProfileService {

    @Autowired
    DatabaseClass db;

    public ProfileService() {

    }

    public List<Profile> getAllProfiles() {
        return new ArrayList<Profile>(db.getProfiles().values());
    }

    public Profile getProfile(String profileName) {
        return db.getProfiles().get(profileName);
    }

    public Profile addProfile(Profile profile) {
        profile.setId(db.getProfiles().size() + 1);
        db.getProfiles().put(profile.getProfileName(), profile);
        return profile;
    }

    public Profile updateProfile(Profile profile) {
        if (profile.getId() <= 0) {
            return null;
        }
        db.getProfiles().put(profile.getProfileName(), profile);
        return profile;
    }

    public Profile deleteProfile(String profileName) {
        return db.getProfiles().remove(profileName);
    }
}
