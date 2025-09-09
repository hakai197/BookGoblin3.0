package com.bookgoblin.server.dao;


import com.bookgoblin.server.model.Tag;

import java.util.List;

public interface TagDao {
    List<Tag> getTags();
    Tag getTagById(int tagId);
    List<Tag> getTagsByBookId(int bookId);
    Tag createTag(Tag tag);
    Tag updateTag(Tag tag);
    boolean deleteTag(int tagId);
    boolean addTagToBook(int tagId, int bookId);
    boolean removeTagFromBook(int tagId, int bookId);
}