package com.example.demo.service;

import com.example.demo.model.Attachment;
import com.example.demo.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttachmentService {
    @Autowired
    private AttachmentRepository attachmentRepository;

    public List<Attachment> findAll(){
        return attachmentRepository.findAll();
    }

    public Attachment save(Attachment attachment){
        return attachmentRepository.save(attachment);
    }

    public void delete(Long id) {
        attachmentRepository.deleteById(id);
    }

    public Optional<Attachment> findById(Long id) {
        return attachmentRepository.findById(id);
    }
}
