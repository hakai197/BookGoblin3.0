package com.bookgoblin.server.dao;

import com.bookgoblin.server.model.Book;

import java.util.List;

public interface BookDao {
    List<Book> getBooks();
    Book getBookById(int bookId);
    List<Book> getBooksByTitle(String title);
    List<Book> getBooksByAuthor(String author);
    List<Book> getBooksByTitleAndAuthor(String title, String author);
    Book createBook(Book book);
    Book updateBook(Book book);
    boolean deleteBook(int bookId);
}