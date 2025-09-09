package com.bookgoblin.server.controller;

import com.bookgoblin.server.dao.UserBookDao;
import com.bookgoblin.server.exception.DaoException;
import com.bookgoblin.server.model.UserBook;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/user-books")
public class UserBookController {

    private final UserBookDao userBookDao;

    public UserBookController(UserBookDao userBookDao) {
        this.userBookDao = userBookDao;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<UserBook> getUserBooks(Principal principal) {
        try {
            String username = principal.getName();
            return userBookDao.getUserBooksByUsername(username);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public UserBook getUserBookById(@PathVariable int id, Principal principal) {
        try {
            UserBook userBook = userBookDao.getUserBookById(id);
            if (userBook == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User book not found");
            }

            // Check if the user owns this user book
            if (!userBook.getUsername().equals(principal.getName())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
            }

            return userBook;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public UserBook addUserBook(@Valid @RequestBody UserBook userBook, Principal principal) {
        try {
            userBook.setUsername(principal.getName());
            return userBookDao.createUserBook(userBook);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public UserBook updateUserBook(@PathVariable int id, @Valid @RequestBody UserBook userBook, Principal principal) {
        try {
            // Check if the user owns this user book
            UserBook existingUserBook = userBookDao.getUserBookById(id);
            if (existingUserBook == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User book not found");
            }

            if (!existingUserBook.getUsername().equals(principal.getName())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
            }

            userBook.setUserBookId(id);
            userBook.setUsername(principal.getName());
            UserBook updatedUserBook = userBookDao.updateUserBook(userBook);

            if (updatedUserBook == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User book not found");
            }

            return updatedUserBook;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void deleteUserBook(@PathVariable int id, Principal principal) {
        try {
            // Check if the user owns this user book
            UserBook userBook = userBookDao.getUserBookById(id);
            if (userBook == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User book not found");
            }

            if (!userBook.getUsername().equals(principal.getName())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
            }

            if (!userBookDao.deleteUserBook(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User book not found");
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }
}
