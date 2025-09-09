package com.bookgoblin.server.dao;

import com.bookgoblin.server.exception.DaoException;
import com.bookgoblin.server.model.UserBook;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserBookDao implements UserBookDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserBookDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<UserBook> getUserBooks() {
        List<UserBook> userBooks = new ArrayList<>();
        String sql = "SELECT ub.user_book_id, ub.user_id, ub.book_id, ub.date_added, " +
                "ub.is_owned, ub.current_status, u.username, b.title, b.author " +
                "FROM user_books ub " +
                "JOIN users u ON ub.user_id = u.user_id " +
                "JOIN books b ON ub.book_id = b.book_id " +
                "ORDER BY ub.date_added DESC";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                UserBook userBook = mapRowToUserBook(results);
                userBooks.add(userBook);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return userBooks;
    }

    @Override
    public UserBook getUserBookById(int userBookId) {
        UserBook userBook = null;
        String sql = "SELECT ub.user_book_id, ub.user_id, ub.book_id, ub.date_added, " +
                "ub.is_owned, ub.current_status, u.username, b.title, b.author " +
                "FROM user_books ub " +
                "JOIN users u ON ub.user_id = u.user_id " +
                "JOIN books b ON ub.book_id = b.book_id " +
                "WHERE ub.user_book_id = ?";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userBookId);
            if (results.next()) {
                userBook = mapRowToUserBook(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return userBook;
    }

    @Override
    public List<UserBook> getUserBooksByUserId(int userId) {
        List<UserBook> userBooks = new ArrayList<>();
        String sql = "SELECT ub.user_book_id, ub.user_id, ub.book_id, ub.date_added, " +
                "ub.is_owned, ub.current_status, u.username, b.title, b.author " +
                "FROM user_books ub " +
                "JOIN users u ON ub.user_id = u.user_id " +
                "JOIN books b ON ub.book_id = b.book_id " +
                "WHERE ub.user_id = ? " +
                "ORDER BY ub.date_added DESC";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            while (results.next()) {
                UserBook userBook = mapRowToUserBook(results);
                userBooks.add(userBook);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return userBooks;
    }

    @Override
    public List<UserBook> getUserBooksByUsername(String username) {
        List<UserBook> userBooks = new ArrayList<>();
        String sql = "SELECT ub.user_book_id, ub.user_id, ub.book_id, ub.date_added, " +
                "ub.is_owned, ub.current_status, u.username, b.title, b.author " +
                "FROM user_books ub " +
                "JOIN users u ON ub.user_id = u.user_id " +
                "JOIN books b ON ub.book_id = b.book_id " +
                "WHERE u.username = ? " +
                "ORDER BY ub.date_added DESC";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
            while (results.next()) {
                UserBook userBook = mapRowToUserBook(results);
                userBooks.add(userBook);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return userBooks;
    }

    @Override
    public UserBook createUserBook(UserBook userBook) {
        UserBook newUserBook = null;
        String sql = "INSERT INTO user_books (user_id, book_id, date_added, is_owned, current_status) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING user_book_id";

        try {
            // First get the user_id from username
            String userIdSql = "SELECT user_id FROM users WHERE username = ?";
            Integer userId = jdbcTemplate.queryForObject(userIdSql, Integer.class, userBook.getUsername());

            if (userId == null) {
                throw new DaoException("User not found");
            }

            int newUserBookId = jdbcTemplate.queryForObject(sql, int.class,
                    userId, userBook.getBookId(), userBook.getDateAdded(),
                    userBook.isOwned(), userBook.getCurrentStatus());

            newUserBook = getUserBookById(newUserBookId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newUserBook;
    }

    @Override
    public UserBook updateUserBook(UserBook userBook) {
        UserBook updatedUserBook = null;
        String sql = "UPDATE user_books SET book_id = ?, date_added = ?, " +
                "is_owned = ?, current_status = ? WHERE user_book_id = ?";

        try {
            int rowsAffected = jdbcTemplate.update(sql, userBook.getBookId(),
                    userBook.getDateAdded(), userBook.isOwned(),
                    userBook.getCurrentStatus(), userBook.getUserBookId());

            if (rowsAffected == 0) {
                return null;
            }

            updatedUserBook = getUserBookById(userBook.getUserBookId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedUserBook;
    }

    @Override
    public boolean deleteUserBook(int userBookId) {
        String sql = "DELETE FROM user_books WHERE user_book_id = ?";

        try {
            int rowsAffected = jdbcTemplate.update(sql, userBookId);
            return rowsAffected > 0;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    private UserBook mapRowToUserBook(SqlRowSet rs) {
        UserBook userBook = new UserBook();
        userBook.setUserBookId(rs.getInt("user_book_id"));
        userBook.setUserId(rs.getInt("user_id"));
        userBook.setBookId(rs.getInt("book_id"));
        userBook.setDateAdded(rs.getDate("date_added").toLocalDate());
        userBook.setOwned(rs.getBoolean("is_owned"));
        userBook.setCurrentStatus(rs.getString("current_status"));
        userBook.setUsername(rs.getString("username"));
        userBook.setBookTitle(rs.getString("title"));
        userBook.setBookAuthor(rs.getString("author"));
        return userBook;
    }
}