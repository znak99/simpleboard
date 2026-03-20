package com.simpleboard.post.dto;

import java.time.LocalDateTime;

public class PostListItem {

    private final Long id;
    private final String title;
    private final String authorName;
    private final LocalDateTime createdAt;

    public PostListItem(Long id, String title, String authorName, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}