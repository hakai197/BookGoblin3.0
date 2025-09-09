package com.bookgoblin.server.dao;

import com.bookgoblin.server.exception.DaoException;
import com.bookgoblin.server.model.Book;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcBookDao implements BookDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcBookDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY title";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                Book book = mapRowToBook(results);
                books.add(book);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return books;
    }

    @Override
    public Book getBookById(int bookId) {
        Book book = null;
        String sql = "SELECT * FROM books WHERE book_id = ?";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, bookId);
            if (results.next()) {
                book = mapRowToBook(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return book;
    }

    @Override
    public List<Book> getBooksByTitle(String title) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title ILIKE ? ORDER BY title";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, "%" + title + "%");
            while (results.next()) {
                Book book = mapRowToBook(results);
                books.add(book);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return books;
    }

    @Override
    public List<Book> getBooksByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE author ILIKE ? ORDER BY author, title";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, "%" + author + "%");
            while (results.next()) {
                Book book = mapRowToBook(results);
                books.add(book);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return books;
    }

    @Override
    public List<Book> getBooksByTitleAndAuthor(String title, String author) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title ILIKE ? AND author ILIKE ? ORDER BY title";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, "%" + title + "%", "%" + author + "%");
            while (results.next()) {
                Book book = mapRowToBook(results);
                books.add(book);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return books;
    }

    @Override
    public Book createBook(Book book) {
        Book newBook = null;
        String sql = "INSERT INTO books (title, author, isbn, cover_image_url, publication_year) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING book_id";

        try {
            int newBookId = jdbcTemplate.queryForObject(sql, int.class,
                    book.getTitle(), book.getAuthor(), book.getIsbn(),
                    book.getCoverImageUrl(), book.getPublicationYear());
            newBook = getBookById(newBookId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newBook;
    }

    @Override
    public Book updateBook(Book book) {
        Book updatedBook = null;
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, " +
                "cover_image_url = ?, publication_year = ? WHERE book_id = ?";

        try {
            int rowsAffected = jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(),
                    book.getIsbn(), book.getCoverImageUrl(), book.getPublicationYear(), book.getBookId());

            if (rowsAffected == 0) {
                return null;
            }

            updatedBook = getBookById(book.getBookId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedBook;
    }

    @Override
    public boolean deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE book_id = ?";

        try {
            int rowsAffected = jdbcTemplate.update(sql, bookId);
            return rowsAffected > 0;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    private Book mapRowToBook(SqlRowSet rs) {
        Book book = new Book();
        book.setBookId(rs.getInt("book_id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setIsbn(rs.getString("isbn"));
        book.setCoverImageUrl(rs.getString("cover_image_url"));

        if (rs.getObject("publication_year") != null) {
            book.setPublicationYear(rs.getInt("publication_year"));
        }

        return book;
    }
}