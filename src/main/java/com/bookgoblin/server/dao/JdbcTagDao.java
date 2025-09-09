package com.bookgoblin.server.dao;


import com.bookgoblin.server.exception.DaoException;
import com.bookgoblin.server.model.Tag;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTagDao implements TagDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTagDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Tag> getTags() {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT * FROM tags ORDER BY name";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                Tag tag = mapRowToTag(results);
                tags.add(tag);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return tags;
    }

    @Override
    public Tag getTagById(int tagId) {
        Tag tag = null;
        String sql = "SELECT * FROM tags WHERE tag_id = ?";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, tagId);
            if (results.next()) {
                tag = mapRowToTag(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return tag;
    }

    @Override
    public List<Tag> getTagsByBookId(int bookId) {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT t.* FROM tags t " +
                "JOIN book_tags bt ON t.tag_id = bt.tag_id " +
                "WHERE bt.book_id = ? " +
                "ORDER BY t.name";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, bookId);
            while (results.next()) {
                Tag tag = mapRowToTag(results);
                tags.add(tag);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return tags;
    }

    @Override
    public Tag createTag(Tag tag) {
        Tag newTag = null;
        String sql = "INSERT INTO tags (name) VALUES (?) RETURNING tag_id";

        try {
            int newTagId = jdbcTemplate.queryForObject(sql, int.class, tag.getName());
            newTag = getTagById(newTagId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newTag;
    }

    @Override
    public Tag updateTag(Tag tag) {
        Tag updatedTag = null;
        String sql = "UPDATE tags SET name = ? WHERE tag_id = ?";

        try {
            int rowsAffected = jdbcTemplate.update(sql, tag.getName(), tag.getTagId());

            if (rowsAffected == 0) {
                return null;
            }

            updatedTag = getTagById(tag.getTagId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedTag;
    }

    @Override
    public boolean deleteTag(int tagId) {
        String sql = "DELETE FROM tags WHERE tag_id = ?";

        try {
            int rowsAffected = jdbcTemplate.update(sql, tagId);
            return rowsAffected > 0;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public boolean addTagToBook(int tagId, int bookId) {
        String sql = "INSERT INTO book_tags (book_id, tag_id) VALUES (?, ?)";

        try {
            int rowsAffected = jdbcTemplate.update(sql, bookId, tagId);
            return rowsAffected > 0;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            return false; // Tag already assigned to book
        }
    }

    @Override
    public boolean removeTagFromBook(int tagId, int bookId) {
        String sql = "DELETE FROM book_tags WHERE book_id = ? AND tag_id = ?";

        try {
            int rowsAffected = jdbcTemplate.update(sql, bookId, tagId);
            return rowsAffected > 0;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    private Tag mapRowToTag(SqlRowSet rs) {
        Tag tag = new Tag();
        tag.setTagId(rs.getInt("tag_id"));
        tag.setName(rs.getString("name"));
        return tag;
    }
}
