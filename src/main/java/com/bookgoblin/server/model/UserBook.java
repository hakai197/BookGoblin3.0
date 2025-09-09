package com.bookgoblin.server.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class UserBook {
    private int userBookId;
    private int userId;
    private int bookId;

    @NotNull(message = "Date added is required")
    private LocalDate dateAdded;

    private boolean isOwned;

    @Pattern(regexp = "^(unread|reading|finished|dnf)$", message = "Status must be one of: unread, reading, finished, dnf")
    private String currentStatus;

    // Additional fields for display
    private String username;
    private String bookTitle;
    private String bookAuthor;

    // Constructors
    public UserBook() {
    }

    public UserBook(int userId, int bookId, LocalDate dateAdded, boolean isOwned, String currentStatus) {
        this.userId = userId;
        this.bookId = bookId;
        this.dateAdded = dateAdded;
        this.isOwned = isOwned;
        this.currentStatus = currentStatus;
    }

    // Getters and Setters
    public int getUserBookId() {
        return userBookId;
    }

    public void setUserBookId(int userBookId) {
        this.userBookId = userBookId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public boolean isOwned() {
        return isOwned;
    }

    public void setOwned(boolean owned) {
        isOwned = owned;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }
}

