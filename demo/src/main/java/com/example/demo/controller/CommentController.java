package com.example.demo.controller;

import com.example.demo.model.Comment;
import com.example.demo.model.Todo;
import com.example.demo.service.CommentService;
import com.example.demo.service.TodoService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private TodoService todoService;

    @PostMapping("/todos/{todoId}/comments/add")
    public String addComment(@PathVariable Long todoId, @RequestParam String content, Authentication authentication) {
        //todo
        Todo todo = todoService.findById(todoId).orElseThrow(() -> new IllegalArgumentException("Invalid todo Id: " + todoId));
        Comment comment = new Comment(content, todo);
        commentService.save(comment);
        return "redirect:/todos";
    }

    @GetMapping("/todos/{todoId}/comments/delete/{commentId}")
    public String deleteComment(@PathVariable Long todoId, @PathVariable Long commentId) {
        commentService.delete(commentId);
        return "redirect:/todos";
    }
}
