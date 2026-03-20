package com.simpleboard.post.dto;

public class PostSearchCondition {

    private String keyword;

    public PostSearchCondition() {
    }

    public PostSearchCondition(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public boolean hasKeyword() {
        return keyword != null && !keyword.trim().isEmpty();
    }
}