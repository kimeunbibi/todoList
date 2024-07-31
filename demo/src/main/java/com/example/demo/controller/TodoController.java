package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.Todo;
import com.example.demo.model.User;
import com.example.demo.service.CategoryService;
import com.example.demo.service.TodoService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private CategoryService categoryService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @PostMapping("/todos/add")
    public String addTodo(@RequestParam String title,
                          @RequestParam String priority,
                          @RequestParam("dueDate") String dueDateStr,
                          @RequestParam Long categoryId,
                          Authentication authentication) {
        LocalDateTime dueDate = LocalDateTime.parse(dueDateStr, formatter);

        User user = userService.findByUsername(authentication.getName());
        Category category = categoryService.findById(categoryId).orElse(null);
        Todo todo = new Todo(title, false, dueDate, priority, user, category);
        todoService.save(todo);
        return "redirect:/todos";
    }

    @Transactional
    @GetMapping("/todos")
    public String listTodos(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        List<Todo> todos = todoService.getTodosByUser(user);
        List<TodoDTO> todoDTOs = todos.stream()
                .map(todo -> new TodoDTO(
                        todo.getId(),
                        todo.getTitle(),
                        todo.getPriority(),
                        todo.getDueDate().format(formatter),
                        todo.isCompleted(),
                        todo.getUser().getUsername(),
                        todo.getCategory() != null ? todo.getCategory().getName() : "No Category"))
                .collect(Collectors.toList());

        List<Category> categories = categoryService.findAll();

        model.addAttribute("todos", todoDTOs);
        model.addAttribute("categories", categories);
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
        private String categoryName;

        public TodoDTO(Long id, String title, String priority, String dueDate, boolean completed, String username, String categoryName) {
            this.id = id;
            this.title = title;
            this.priority = priority;
            this.dueDate = dueDate;
            this.completed = completed;
            this.username = username;
            this.categoryName = categoryName;
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

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
    }
}
