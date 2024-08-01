package com.example.demo.controller;

import com.example.demo.dto.TodoDTO;
import com.example.demo.model.Category;
import com.example.demo.model.Comment;
import com.example.demo.model.Attachment;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
                        todo.getCategory() != null ? todo.getCategory().getName() : "No Category",
                        //todo
                        todo.getComments(),
                        todo.getAttachments()
                        ))
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

}
