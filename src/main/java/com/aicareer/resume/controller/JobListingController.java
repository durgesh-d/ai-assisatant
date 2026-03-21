package com.aicareer.resume.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/jobs")
@CrossOrigin("*")
public class JobListingController {

    @Value("${rapidapi.key:}")
    private String rapidApiKey;

    @PostMapping("/search")
    public String searchJobs(@RequestBody Map<String, String> body) {

        String role = body.getOrDefault("role", "Java Developer");
        String location = body.getOrDefault("location", "India");
        String page = body.getOrDefault("page", "1");

        // RapidAPI key नसेल तर mock data
        if (rapidApiKey == null || rapidApiKey.isEmpty()) {
            return getMockJobs(role, location);
        }

        try {
            String url = "https://jsearch.p.rapidapi.com/search?query="
                    + java.net.URLEncoder.encode(role + " in " + location, "UTF-8")
                    + "&page=" + page
                    + "&num_pages=1"
                    + "&date_posted=month";

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-RapidAPI-Key", rapidApiKey);
            headers.set("X-RapidAPI-Host", "jsearch.p.rapidapi.com");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class);

            return response.getBody();

        } catch (Exception e) {
            e.printStackTrace();
            return getMockJobs(role, location);
        }
    }

    // Mock data (API key नसताना)
    private String getMockJobs(String role, String location) {
        return """
        {
          "data": [
            {
              "job_id": "1",
              "job_title": "%s Developer",
              "employer_name": "TechCorp Solutions",
              "employer_logo": null,
              "job_city": "%s",
              "job_country": "India",
              "job_employment_type": "FULLTIME",
              "job_posted_at_datetime_utc": "2026-03-20T00:00:00",
              "job_apply_link": "https://www.linkedin.com/jobs",
              "job_description": "We are looking for an experienced %s Developer to join our team. Must have strong knowledge of core concepts and frameworks.",
              "job_min_salary": 600000,
              "job_max_salary": 1200000,
              "job_salary_currency": "INR"
            },
            {
              "job_id": "2",
              "job_title": "Senior %s Engineer",
              "employer_name": "InnovateTech Pvt Ltd",
              "employer_logo": null,
              "job_city": "Bangalore",
              "job_country": "India",
              "job_employment_type": "FULLTIME",
              "job_posted_at_datetime_utc": "2026-03-19T00:00:00",
              "job_apply_link": "https://www.naukri.com",
              "job_description": "Senior %s Engineer needed with 3+ years experience. Work on cutting-edge projects.",
              "job_min_salary": 1200000,
              "job_max_salary": 2000000,
              "job_salary_currency": "INR"
            },
            {
              "job_id": "3",
              "job_title": "%s Specialist",
              "employer_name": "StartupXYZ",
              "employer_logo": null,
              "job_city": "Pune",
              "job_country": "India",
              "job_employment_type": "FULLTIME",
              "job_posted_at_datetime_utc": "2026-03-18T00:00:00",
              "job_apply_link": "https://www.indeed.com",
              "job_description": "Join our growing startup as a %s Specialist. Great work culture and growth opportunities.",
              "job_min_salary": 800000,
              "job_max_salary": 1500000,
              "job_salary_currency": "INR"
            },
            {
              "job_id": "4",
              "job_title": "%s Developer - Remote",
              "employer_name": "GlobalTech Inc",
              "employer_logo": null,
              "job_city": "Remote",
              "job_country": "India",
              "job_employment_type": "FULLTIME",
              "job_posted_at_datetime_utc": "2026-03-17T00:00:00",
              "job_apply_link": "https://www.glassdoor.com",
              "job_description": "Remote position for %s Developer. Work from anywhere in India.",
              "job_min_salary": 1000000,
              "job_max_salary": 1800000,
              "job_salary_currency": "INR"
            },
            {
              "job_id": "5",
              "job_title": "Junior %s Developer",
              "employer_name": "CodeBase Solutions",
              "employer_logo": null,
              "job_city": "Mumbai",
              "job_country": "India",
              "job_employment_type": "FULLTIME",
              "job_posted_at_datetime_utc": "2026-03-16T00:00:00",
              "job_apply_link": "https://www.foundit.in",
              "job_description": "Fresh graduates welcome! Junior %s Developer position with great learning opportunities.",
              "job_min_salary": 400000,
              "job_max_salary": 700000,
              "job_salary_currency": "INR"
            }
          ]
        }
        """.formatted(role, location, role, role, role, role, role, role, role, role);
    }
}