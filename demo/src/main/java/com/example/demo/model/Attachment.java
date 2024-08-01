package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    private Todo todo;

    public Attachment(Long id, String fileName, String filePath, Todo todo) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.todo = todo;
    }

    public Attachment() {

    }

    public Attachment(String originalFilename, String filePath, Todo todo) {
        this.fileName = originalFilename;
        this.filePath = filePath;
        this.todo = todo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Todo getTodo() {
        return todo;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;
    }
}
