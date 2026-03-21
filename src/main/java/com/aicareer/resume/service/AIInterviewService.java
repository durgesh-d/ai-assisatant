package com.aicareer.resume.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AIInterviewService {

    @Value("${groq.api.key}")
    private String apiKey;

    public String generateQuestions(String role) {

        String url = "https://api.groq.com/openai/v1/chat/completions";
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama-3.1-8b-instant");
        body.put("max_tokens", 2000);

        List<Map<String, String>> messages = new ArrayList<>();

        Map<String, String> system = new HashMap<>();
        system.put("role", "system");
        system.put("content", "You are a technical interviewer.");

        Map<String, String> user = new HashMap<>();
        user.put("role", "user");
        user.put("content",
                "Generate exactly 10 technical interview questions for the role: " + role +
                "\n\nFormat: numbered list 1-10, one question per line, no extra text.");

        messages.add(system);
        messages.add(user);
        body.put("messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, request, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            return root
                    .get("choices")
                    .get(0)
                    .get("message")
                    .get("content")
                    .asText();

        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error generating questions: " + e.getMessage();
        }
    }
}