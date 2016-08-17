package com.jersey.ch01.server.messenger.service;



import com.jersey.ch01.server.messenger.database.DatabaseClass;
import com.jersey.ch01.server.messenger.model.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
