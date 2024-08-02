package com.example.demo.dto;

import com.example.demo.model.Todo;

public class AttachmentDTO {
    private Long id;
    private String fileName;
    private String filePath;
    private Todo todo;

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public Todo getTodo() {
        return todo;
    }
}
