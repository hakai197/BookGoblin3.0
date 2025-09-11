package com.bookgoblin.server.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Book {
    private int bookId;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 100, message = "Author name cannot exceed 100 characters")
    private String author;

    @Size(max = 20, message = "ISBN cannot exceed 20 characters")
    private String isbn;

    private String coverImageUrl;
    private Integer publicationYear;

    // Constructors
    public Book() {
    }

    public Book(String title, String author, String isbn, String coverImageUrl, Integer publicationYear) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.coverImageUrl = coverImageUrl;
        this.publicationYear = publicationYear;
    }

    // Getters and Setters
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }
}
