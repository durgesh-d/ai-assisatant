package com.aicareer.resume.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.aicareer.resume.model.ResumeHistory;
import com.aicareer.resume.repository.ResumeHistoryRepository;
import com.aicareer.resume.security.JwtUtil;
import com.aicareer.resume.service.AIService;
import com.aicareer.resume.service.PdfService;

@RestController
@RequestMapping("/resume")
@CrossOrigin
public class ResumeController {

    @Autowired private PdfService pdfService;
    @Autowired private AIService aiService;
    @Autowired private ResumeHistoryRepository historyRepository;
    @Autowired private JwtUtil jwtUtil;

    private String getEmail(String header) {
        if (header == null || !header.startsWith("Bearer ")) return null;
        return jwtUtil.getEmailFromToken(header.substring(7));
    }

    // 🔥 TEXT ANALYZE — auto save
    @PostMapping("/analyze")
    public String analyze(
            @RequestHeader("Authorization") String header,
            @RequestBody Map<String,String> body) {

        String resume = body.get("resume");
        if(resume == null || resume.isEmpty()) return "❌ Please enter resume";

        String result = aiService.analyzeResume(resume);

        // Auto save to history
        try {
            String email = getEmail(header);
            if (email != null) {
                ResumeHistory history = new ResumeHistory();
                history.setUserEmail(email);
                history.setResumeText(resume.length() > 4000 ? resume.substring(0,4000) : resume);
                history.setAnalysisResult(result.length() > 9000 ? result.substring(0,9000) : result);
                history.setTemplateUsed("analyze");
                java.util.regex.Matcher m = java.util.regex.Pattern
                        .compile("(\\d{1,3})\\s*/\\s*100").matcher(result);
                if (m.find()) history.setScore(Integer.parseInt(m.group(1)));
                historyRepository.save(history);
            }
        } catch(Exception e) { e.printStackTrace(); }

        return result;
    }

    // 🔥 JOB MATCH
    @PostMapping("/match")
    public String match(@RequestBody Map<String,String> body) {
        String resume = body.get("resume");
        String job = body.get("job");
        if(resume == null || job == null) return "❌ Missing input";
        return aiService.matchResumeWithJob(resume, job);
    }

    // 🔥 COVER LETTER
    @PostMapping("/cover-letter")
    public String cover(@RequestBody Map<String,String> body) {
        return aiService.generateCoverLetter(body.get("resume"), body.get("job"));
    }

    // 🔥 PDF ANALYZE — auto save
    @PostMapping("/analyze-pdf")
    public Map<String,String> analyzePDF(
            @RequestHeader("Authorization") String header,
            @RequestParam("file") MultipartFile file) {
        try {
            String text = pdfService.extractText(file);
            String result = aiService.analyzeResume(text);

            // Auto save
            try {
                String email = getEmail(header);
                if (email != null) {
                    ResumeHistory history = new ResumeHistory();
                    history.setUserEmail(email);
                    history.setResumeText(text.length() > 4000 ? text.substring(0,4000) : text);
                    history.setAnalysisResult(result.length() > 9000 ? result.substring(0,9000) : result);
                    history.setTemplateUsed("pdf");
                    java.util.regex.Matcher m = java.util.regex.Pattern
                            .compile("(\\d{1,3})\\s*/\\s*100").matcher(result);
                    if (m.find()) history.setScore(Integer.parseInt(m.group(1)));
                    historyRepository.save(history);
                }
            } catch(Exception e) { e.printStackTrace(); }

            return Map.of("resume", text, "result", result);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("result","Error reading PDF");
        }
    }
}