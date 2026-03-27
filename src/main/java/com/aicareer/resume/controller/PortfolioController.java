package com.aicareer.resume.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aicareer.resume.service.AIService;
import com.aicareer.resume.service.PdfService;

@RestController
@RequestMapping("/portfolio")
@CrossOrigin
public class PortfolioController
{

@Autowired
PdfService pdfService;

@Autowired
AIService aiService;

@PostMapping("/generate")
public String generatePortfolio(@RequestBody Map<String,String> body)
{

    String resume = body.get("resume");

    return aiService.generatePortfolio(resume);
}

}