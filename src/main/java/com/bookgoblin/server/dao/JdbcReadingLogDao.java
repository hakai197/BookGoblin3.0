package com.bookgoblin.server.dao;


import com.bookgoblin.server.exception.DaoException;
import com.bookgoblin.server.model.ReadingLog;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcReadingLogDao implements ReadingLogDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReadingLogDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ReadingLog> getReadingLogs() {
        List<ReadingLog> readingLogs = new ArrayList<>();
        String sql = "SELECT rl.log_id, rl.user_book_id, rl.start_date, rl.end_date, " +
                "rl.rating, rl.notes, ub.user_id, u.username, b.title, b.author " +
                "FROM reading_logs rl " +
                "JOIN user_books ub ON rl.user_book_id = ub.user_book_id " +
                "JOIN users u ON ub.user_id = u.user_id " +
                "JOIN books b ON ub.book_id = b.book_id " +
                "ORDER BY rl.start_date DESC";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                ReadingLog readingLog = mapRowToReadingLog(results);
                readingLogs.add(readingLog);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return readingLogs;
    }

    @Override
    public ReadingLog getReadingLogById(int logId) {
        ReadingLog readingLog = null;
        String sql = "SELECT rl.log_id, rl.user_book_id, rl.start_date, rl.end_date, " +
                "rl.rating, rl.notes, ub.user_id, u.username, b.title, b.author " +
                "FROM reading_logs rl " +
                "JOIN user_books ub ON rl.user_book_id = ub.user_book_id " +
                "JOIN users u ON ub.user_id = u.user_id " +
                "JOIN books b ON ub.book_id = b.book_id " +
                "WHERE rl.log_id = ?";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, logId);
            if (results.next()) {
                readingLog = mapRowToReadingLog(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return readingLog;
    }

    @Override
    public List<ReadingLog> getReadingLogsByUserBookId(int userBookId) {
        List<ReadingLog> readingLogs = new ArrayList<>();
        String sql = "SELECT rl.log_id, rl.user_book_id, rl.start_date, rl.end_date, " +
                "rl.rating, rl.notes, ub.user_id, u.username, b.title, b.author " +
                "FROM reading_logs rl " +
                "JOIN user_books ub ON rl.user_book_id = ub.user_book_id " +
                "JOIN users u ON ub.user_id = u.user_id " +
                "JOIN books b ON ub.book_id = b.book_id " +
                "WHERE rl.user_book_id = ? " +
                "ORDER BY rl.start_date DESC";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userBookId);
            while (results.next()) {
                ReadingLog readingLog = mapRowToReadingLog(results);
                readingLogs.add(readingLog);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return readingLogs;
    }

    @Override
    public ReadingLog createReadingLog(ReadingLog readingLog) {
        ReadingLog newReadingLog = null;
        String sql = "INSERT INTO reading_logs (user_book_id, start_date, end_date, rating, notes) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING log_id";

        try {
            int newLogId = jdbcTemplate.queryForObject(sql, int.class,
                    readingLog.getUserBookId(), readingLog.getStartDate(),
                    readingLog.getEndDate(), readingLog.getRating(), readingLog.getNotes());

            newReadingLog = getReadingLogById(newLogId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newReadingLog;
    }

    @Override
    public ReadingLog updateReadingLog(ReadingLog readingLog) {
        ReadingLog updatedReadingLog = null;
        String sql = "UPDATE reading_logs SET user_book_id = ?, start_date = ?, " +
                "end_date = ?, rating = ?, notes = ? WHERE log_id = ?";

        try {
            int rowsAffected = jdbcTemplate.update(sql, readingLog.getUserBookId(),
                    readingLog.getStartDate(), readingLog.getEndDate(),
                    readingLog.getRating(), readingLog.getNotes(), readingLog.getLogId());

            if (rowsAffected == 0) {
                return null;
            }

            updatedReadingLog = getReadingLogById(readingLog.getLogId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedReadingLog;
    }

    @Override
    public boolean deleteReadingLog(int logId) {
        String sql = "DELETE FROM reading_logs WHERE log_id = ?";

        try {
            int rowsAffected = jdbcTemplate.update(sql, logId);
            return rowsAffected > 0;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public boolean hasUserAccessToUserBook(String username, int userBookId) {
        String sql = "SELECT COUNT(*) FROM user_books ub " +
                "JOIN users u ON ub.user_id = u.user_id " +
                "WHERE ub.user_book_id = ? AND u.username = ?";

        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userBookId, username);
            return count != null && count > 0;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
    }

    @Override
    public boolean hasUserAccessToReadingLog(String username, int logId) {
        String sql = "SELECT COUNT(*) FROM reading_logs rl " +
                "JOIN user_books ub ON rl.user_book_id = ub.user_book_id " +
                "JOIN users u ON ub.user_id = u.user_id " +
                "WHERE rl.log_id = ? AND u.username = ?";

        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, logId, username);
            return count != null && count > 0;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
    }

    private ReadingLog mapRowToReadingLog(SqlRowSet rs) {
        ReadingLog readingLog = new ReadingLog();
        readingLog.setLogId(rs.getInt("log_id"));
        readingLog.setUserBookId(rs.getInt("user_book_id"));
        readingLog.setStartDate(rs.getDate("start_date").toLocalDate());

        if (rs.getDate("end_date") != null) {
            readingLog.setEndDate(rs.getDate("end_date").toLocalDate());
        }

        if (rs.getObject("rating") != null) {
            readingLog.setRating(rs.getInt("rating"));
        }

        readingLog.setNotes(rs.getString("notes"));
        readingLog.setUserId(rs.getInt("user_id"));
        readingLog.setUsername(rs.getString("username"));
        readingLog.setBookTitle(rs.getString("title"));
        readingLog.setBookAuthor(rs.getString("author"));

        return readingLog;
    }
}