package com.TextHub.TextHub.Entity;


public class PostDTO {
    private Long postId;
    private String title;
    private String content;
    
// Геттеры и сеттеры
    public Long getPostId() {
        return postId;
    }

    public void setId(Long postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}