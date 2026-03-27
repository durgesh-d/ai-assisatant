package com.aicareer.resume.controller;

import com.aicareer.resume.model.User;
import com.aicareer.resume.repository.ResumeHistoryRepository;
import com.aicareer.resume.repository.UserRepository;
import com.aicareer.resume.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/profile")
@CrossOrigin("*")
public class UserProfileController
{

    @Autowired private UserRepository userRepository;
    @Autowired private ResumeHistoryRepository historyRepository;
    @Autowired private JwtUtil jwtUtil;

    private String getEmail(String header) {
        if (header == null || !header.startsWith("Bearer ")) return null;
        return jwtUtil.getEmailFromToken(header.substring(7));
    }

    // profile gate sati
    @GetMapping("/me")
    public Map<String, Object> getProfile(
            @RequestHeader("Authorization") String header)
    {

        String email = getEmail(header);
        Map<String, Object> result = new HashMap<>();

        if (email == null) 
        {
            result.put("error", "Unauthorized");
            return result;
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            result.put("error", "User not found");
            return result;
        }

        // User mahiti sati
        result.put("name", user.getName());
        result.put("email", user.getEmail());
        result.put("id", user.getId());

        // Stats from history
        var history = historyRepository
                .findByUserEmailOrderByCreatedAtDesc(email);

        result.put("totalAnalyses", history.size());

        // Average score
        double avgScore = history.stream()
                .filter(h -> h.getScore() > 0)
                .mapToInt(h -> h.getScore())
                .average()
                .orElse(0.0);
        result.put("avgScore", Math.round(avgScore));

        // Best score
        int bestScore = history.stream()
                .mapToInt(h -> h.getScore())
                .max()
                .orElse(0);
        result.put("bestScore", bestScore);

        // PDF vs Text count
        long pdfCount = history.stream()
                .filter(h -> "pdf".equals(h.getTemplateUsed()))
                .count();
        result.put("pdfAnalyses", pdfCount);
        result.put("textAnalyses", history.size() - pdfCount);

        return result;
    }

    // UPDATE name
    @PutMapping("/update")
    public Map<String, String> updateProfile(
            @RequestHeader("Authorization") String header,
            @RequestBody Map<String, String> body)
    {

        String email = getEmail(header);
        Map<String, String> result = new HashMap<>();

        if (email == null) {
            result.put("error", "Unauthorized");
            return result;
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            result.put("error", "User not found");
            return result;
        }

        String newName = body.get("name");
        if (newName != null && !newName.trim().isEmpty()) 
        {
            user.setName(newName.trim());
            userRepository.save(user);
            result.put("message", "Profile updated!");
            result.put("name", newName.trim());
        }
        else
        {
            result.put("error", "Name required");
        }

        return result;
    }
}