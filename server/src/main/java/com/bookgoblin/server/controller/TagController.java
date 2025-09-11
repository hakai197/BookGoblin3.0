package com.bookgoblin.server.controller;

import com.bookgoblin.server.dao.TagDao;
import com.bookgoblin.server.exception.DaoException;
import com.bookgoblin.server.model.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/tags")
public class TagController {

    private final TagDao tagDao;

    public TagController(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @GetMapping
    public List<Tag> getAllTags() {
        try {
            return tagDao.getTags();
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable int id) {
        try {
            Tag tag = tagDao.getTagById(id);
            if (tag == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found");
            }
            return tag;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @GetMapping("/book/{bookId}")
    public List<Tag> getTagsByBookId(@PathVariable int bookId) {
        try {
            return tagDao.getTagsByBookId(bookId);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Tag createTag(@Valid @RequestBody Tag tag) {
        try {
            return tagDao.createTag(tag);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Tag updateTag(@PathVariable int id, @Valid @RequestBody Tag tag) {
        try {
            tag.setTagId(id);
            Tag updatedTag = tagDao.updateTag(tag);
            if (updatedTag == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found");
            }
            return updatedTag;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTag(@PathVariable int id) {
        try {
            if (!tagDao.deleteTag(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found");
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @PostMapping("/{tagId}/book/{bookId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public void addTagToBook(@PathVariable int tagId, @PathVariable int bookId) {
        try {
            if (!tagDao.addTagToBook(tagId, bookId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag already assigned to book");
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @DeleteMapping("/{tagId}/book/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void removeTagFromBook(@PathVariable int tagId, @PathVariable int bookId) {
        try {
            if (!tagDao.removeTagFromBook(tagId, bookId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not assigned to book");
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }
}
