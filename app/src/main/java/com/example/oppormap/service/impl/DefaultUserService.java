package com.example.oppormap.service.impl;

import com.example.oppormap.model.entity.User;
import com.example.oppormap.service.UserService;
import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;


public class DefaultUserService implements UserService {
    private static final String ENDPOINT = "https://70rhmqg3vh.execute-api.us-east-2.amazonaws.com/dev/users";
    private static final Gson gson = new Gson();

    @Override
    public User create(User user) {
        try {
            String body = Unirest.post(ENDPOINT)
                    .body(gson.toJson(user))
                    .asString()
                    .getBody();
            return gson.fromJson(body, User.class);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User update(User user) {
        try {
            String body = Unirest.post(ENDPOINT)
                    .body(gson.toJson(user))
                    .asString()
                    .getBody();
            return gson.fromJson(body, User.class);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User get(String userId) {
        try {
            String body = Unirest.get(ENDPOINT + "?id=" + userId)
                    .asString()
                    .getBody();
            return gson.fromJson(body, User.class);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(String userId) {
        try {
            Unirest.get(ENDPOINT + "?id=" + userId)
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
