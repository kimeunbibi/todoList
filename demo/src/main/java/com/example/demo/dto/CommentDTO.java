package com.example.demo.dto;

import com.example.demo.model.Todo;

import java.time.LocalDateTime;

public class CommentDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Todo todo;

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Todo getTodo() {
        return todo;
    }
}
