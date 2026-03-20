package com.simpleboard.comment.dto;

import java.time.LocalDateTime;

public class CommentView {

    private final Long id;
    private final String content;
    private final Long authorId;
    private final String authorName;
    private final LocalDateTime createdAt;

    public CommentView(Long id, String content, Long authorId, String authorName, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}