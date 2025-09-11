package com.bookgoblin.server.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Tag {
    private int tagId;

    @NotBlank(message = "Tag name is required")
    @Size(max = 50, message = "Tag name cannot exceed 50 characters")
    private String name;

    // Constructors
    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    // Getters and Setters
    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}