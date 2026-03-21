package com.aicareer.resume.controller;

import com.aicareer.resume.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/template")
@CrossOrigin("*")
public class TemplateController {

    @Autowired
    private AIService aiService;

    @PostMapping("/generate")
    public String generateTemplate(@RequestBody Map<String, String> body) {
        String templateId = body.getOrDefault("templateId", "modern");
        String userData = body.getOrDefault("userData", "");

        if (userData.trim().isEmpty()) {
            return "❌ User data required";
        }

        return aiService.generateResumeTemplate(templateId, userData);
    }
}