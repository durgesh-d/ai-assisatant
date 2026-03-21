package com.aicareer.resume.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class JobController
{

    @PostMapping("/recommend")
    public String recommendJobs(@RequestBody String skills)
    {

        if(skills.contains("Java")) 
        {
            return "Java Backend Developer, Spring Boot Developer, API Developer";
        }

        return "Software Developer";
    }
}