package com.bookgoblin.server.dao;



import com.bookgoblin.server.model.User;

import java.util.List;

public interface UserDao {

    List<User> getUsers();

    User getUserById(int userId);

    User getUserByUsername(String username);

    User createUser(User newUser);
}
