package com.example.demo.controller;

import com.example.demo.model.Attachment;
import com.example.demo.model.Todo;
import com.example.demo.service.AttachmentService;
import com.example.demo.service.TodoService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private TodoService todoService;

    @PostMapping("/todos/{todoId}/attachments/add")
    public String addAttachment(@PathVariable Long todoId, @RequestParam("file") MultipartFile file, Authentication authentication) throws IOException {
        Todo todo = todoService.findById(todoId).orElseThrow(() -> new IllegalArgumentException("Invalid todo Id: " + todoId));

        // 파일 저장 경로 지정
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 파일 이름에 UUID를 붙여 고유하게 만듦
        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.write(filePath, file.getBytes());

        // Attachment 엔티티 생성 및 저장
        Attachment attachment = new Attachment(uniqueFileName, filePath.toString(), todo);
        attachmentService.save(attachment);

        return "redirect:/todos";
    }

    @GetMapping("/todos/{todoId}/attachments/delete/{attachmentId}")
    public String deleteAttachment(@PathVariable Long todoId, @PathVariable Long attachmentId) {
        attachmentService.delete(attachmentId);
        return "redirect:/todos";
    }

    @GetMapping("todos/{todoId}/attachments/download/{attatchmentId}")
    @ResponseBody
    public ResponseEntity<Resource> downloadAttachment(@PathVariable Long todoId, @PathVariable Long attatchmentId) throws MalformedURLException {
        Attachment attachment = attachmentService.findById(attatchmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid attachment Id: " + attatchmentId));

        Path filePath = Paths.get(attachment.getFilePath());
        Resource resource = new UrlResource(filePath.toUri());

        if(!resource.exists() || !resource.isReadable()) {
            throw new IllegalArgumentException("File not found or is not readable: " + attachment.getFilePath());
        }

        String encodedFileName = URLEncoder.encode(attachment.getFileName(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + encodedFileName + "\"")
                .body(resource);
    }
}
