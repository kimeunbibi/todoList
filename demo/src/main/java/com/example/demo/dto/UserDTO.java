package com.example.demo.dto;

import com.example.demo.model.Todo;

import java.util.List;

public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String role;
    private List<Todo> todos;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public List<Todo> getTodos() {
        return todos;
    }
}
