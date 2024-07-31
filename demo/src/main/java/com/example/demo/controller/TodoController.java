package com.example.demo.controller;

import com.example.demo.model.Todo;
import com.example.demo.model.User;
import com.example.demo.service.TodoService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class TodoController {
    @Autowired
    private TodoService todoService;

    @Autowired
    private UserService userService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @PostMapping("/todos/add")
    public String addTodo(@RequestParam String title,
                          @RequestParam String priority,
                          @RequestParam("dueDate") String dueDateStr,
                          Authentication authentication) {
        LocalDateTime dueDate = LocalDateTime.parse(dueDateStr, formatter);

        User user = userService.findByUsername(authentication.getName());
        Todo todo = new Todo(title, false, dueDate, priority, user);
        todoService.save(todo);
        return "redirect:/todos";
    }

    @GetMapping("/todos")
    public String listTodos(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        List<Todo> todos = todoService.getTodosByUser(user);
        List<TodoDTO> todoDTOs = todos.stream()
                .map(todo -> new TodoDTO(todo.getId(), todo.getTitle(), todo.getPriority(), todo.getDueDate().format(formatter), todo.isCompleted(), todo.getUser().getUsername()))
                .collect(Collectors.toList());
        model.addAttribute("todos", todoDTOs);
        return "todos";
    }

    @GetMapping("todos/delete/{id}")
    public String deleteTodo(@PathVariable("id") Long id) {
        todoService.delete(id);
        return "redirect:/todos";
    }

    @GetMapping("todos/complete/{id}")
    public String completeTodo(@PathVariable("id") Long id) {
        Optional<Todo> todoOptional = todoService.findById(id);
        if(todoOptional.isPresent()) {
            Todo todo = todoOptional.get();
            todo.setCompleted(true);
            todoService.save(todo);
        }
        return "redirect:/todos";
    }

    public static class TodoDTO {
        private Long id;
        private String title;
        private String priority;
        private String dueDate;
        private boolean completed;
        private String username;

        public TodoDTO(Long id, String title, String priority, String dueDate, boolean completed, String username) {
            this.id = id;
            this.title = title;
            this.priority = priority;
            this.dueDate = dueDate;
            this.completed = completed;
            this.username = username;
        }

        // Getters and setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        public String getDueDate() {
            return dueDate;
        }

        public void setDueDate(String dueDate) {
            this.dueDate = dueDate;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
