package com.example.oppormap.service.impl;

import com.example.oppormap.model.entity.User;
import com.example.oppormap.service.UserService;

import kong.unirest.Unirest;
import kong.unirest.UnirestException;

public class DefaultUserService implements UserService {
    private static final String ENDPOINT = "https://70rhmqg3vh.execute-api.us-east-2.amazonaws.com/dev/users";

    @Override
    public User create(User user) {
        try {
            return Unirest.post(ENDPOINT)
                    .body(user)
                    .asObject(User.class)
                    .getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User update(User user) {
        try {
            return Unirest.put(ENDPOINT)
                    .body(user)
                    .asObject(User.class)
                    .getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User get(String userId) {
        try {
            return Unirest.get(ENDPOINT)
                    .queryString("id", userId)
                    .asObject(User.class)
                    .getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(String userId) {
        try {
            Unirest.delete(ENDPOINT)
                    .queryString("id", userId).asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
