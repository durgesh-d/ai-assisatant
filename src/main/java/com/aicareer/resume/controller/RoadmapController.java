package com.aicareer.resume.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roadmap")
public class RoadmapController
{

    @GetMapping("/java")
    public String javaRoadmap() 
    {

        return """
        Step 1: Learn Core Java
        Step 2: Learn JDBC
        Step 3: Learn Spring Boot
        Step 4: Build REST APIs
        Step 5: Learn Microservices
        Step 6: Learn Docker
        """;
    }
}