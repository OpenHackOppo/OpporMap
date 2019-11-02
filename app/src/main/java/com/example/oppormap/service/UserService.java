package com.example.oppormap.service;

import com.example.oppormap.model.entity.User;

public interface UserService {

    /**
     * Create new user
     * @param user User
     * @return Newly created user
     */
    User create(User user);

    /**
     * Update user
     * @param user User
     * @return Newly updated user
     */
    User update(User user);

    /**
     * Find user its id
     * @param userId User id
     * @return Found user, null otherwise
     */
    User get(String userId);

    /**
     * Delete user
     * @param userId User id
     */
    void delete(String userId);
}
