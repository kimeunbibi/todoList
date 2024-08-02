package com.example.demo.dto;

import com.example.demo.model.Attachment;
import com.example.demo.model.Comment;

import java.util.List;

public class TodoDTO {
    private Long id;
    private String title;
    private String priority;
    private String dueDate;
    private boolean completed;
    private String username;
    private String categoryName;
    private List<Comment> comments;
    private List<Attachment> attachments;


    public TodoDTO(Long id, String title, String priority, String dueDate, boolean completed, String username, String categoryName, List<Comment> comments, List<Attachment> attachments) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.dueDate = dueDate;
        this.completed = completed;
        this.username = username;
        this.categoryName = categoryName;
        this.comments = comments;
        this.attachments = attachments;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPriority() {
        return priority;
    }

    public String getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getUsername() {
        return username;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }
}
