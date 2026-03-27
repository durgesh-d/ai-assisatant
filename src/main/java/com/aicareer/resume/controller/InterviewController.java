package com.aicareer.resume.controller;

import com.aicareer.resume.service.AIInterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/interview")
@CrossOrigin("*")
public class InterviewController 
{

    @Autowired
    private AIInterviewService service;

    @PostMapping("/generate")
    public String generateQuestions(@RequestBody Map<String, String> body)
    {
        String role = body.get("role");
        if (role == null || role.trim().isEmpty())
        {
            return "❌ Role required";
        }
        return service.generateQuestions(role);
    }
}