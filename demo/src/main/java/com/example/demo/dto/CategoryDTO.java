package com.example.demo.dto;

import com.example.demo.model.Todo;

import java.util.List;

public class CategoryDTO {
    private Long id;
    private String name;
    private List<Todo> todos;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Todo> getTodos() {
        return todos;
    }
}
