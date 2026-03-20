package com.simpleboard.comment.dto;

import jakarta.validation.constraints.NotBlank;

public class CommentForm {

    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;

    public CommentForm() {
    }

    public CommentForm(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}