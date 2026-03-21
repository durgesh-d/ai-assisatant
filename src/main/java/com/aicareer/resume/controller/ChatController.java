package com.aicareer.resume.controller;

import com.aicareer.resume.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/chat")
@CrossOrigin("*")
public class ChatController {

    @Autowired
    private AIService aiService;

    @PostMapping("/ask")
    public String ask(@RequestBody Map<String, String> body) {
        String message = body.get("message");
        String context = body.getOrDefault("context", "");

        if (message == null || message.trim().isEmpty()) {
            return "❌ Message required";
        }

        return aiService.chat(message, context);
    }
}