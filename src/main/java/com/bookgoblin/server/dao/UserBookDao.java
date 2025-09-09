package com.bookgoblin.server.dao;


import com.bookgoblin.server.model.UserBook;

import java.util.List;

public interface UserBookDao {
    List<UserBook> getUserBooks();
    UserBook getUserBookById(int userBookId);
    List<UserBook> getUserBooksByUserId(int userId);
    List<UserBook> getUserBooksByUsername(String username);
    UserBook createUserBook(UserBook userBook);
    UserBook updateUserBook(UserBook userBook);
    boolean deleteUserBook(int userBookId);
}
