package com.aicareer.resume.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aicareer.resume.model.ResumeHistory;
import com.aicareer.resume.repository.ResumeHistoryRepository;
import com.aicareer.resume.security.JwtUtil;

@RestController
@RequestMapping("/history")
@CrossOrigin("*")
public class ResumeHistoryController
{

    @Autowired
    private ResumeHistoryRepository repository;

    @Autowired
    private JwtUtil jwtUtil;

    // gmail token sati
    private String getEmail(String header)
    {
        if (header == null || !header.startsWith("Bearer ")) return null;
        String token = header.substring(7);
        return jwtUtil.getEmailFromToken(token);
    }

    //histry sati
    @GetMapping("/all")
    public List<ResumeHistory> getHistory(
            @RequestHeader("Authorization") String header)
    {
        String email = getEmail(header);
        if (email == null) return List.of();
        return repository.findByUserEmailOrderByCreatedAtDesc(email);
    }

    // save histroy sati
    @PostMapping("/save")
    public ResumeHistory saveHistory(
            @RequestHeader("Authorization") String header,
            @RequestBody Map<String, String> body) 
    {

        String email = getEmail(header);
        if (email == null) return null;

        ResumeHistory history = new ResumeHistory();
        history.setUserEmail(email);
        history.setResumeText(body.getOrDefault("resumeText", ""));
        history.setAnalysisResult(body.getOrDefault("analysisResult", ""));
        history.setTemplateUsed(body.getOrDefault("templateUsed", "analyze"));

       // score extract
        String result = body.getOrDefault("analysisResult", "");
        java.util.regex.Matcher m = java.util.regex.Pattern
                .compile("(\\d{1,3})\\s*/\\s*100")
                .matcher(result);
        if (m.find())
        {
            history.setScore(Integer.parseInt(m.group(1)));
        }

        return repository.save(history);
    }

    // deleted hestru sati
    
    @DeleteMapping("/delete/{id}")
    public Map<String, String> deleteHistory(@PathVariable Long id) {
        repository.deleteById(id);
        return Map.of("message", "Deleted");
    }
}