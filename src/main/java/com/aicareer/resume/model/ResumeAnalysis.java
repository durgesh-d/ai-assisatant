package com.aicareer.resume.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ResumeAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 5000)
    private String resumeText;

    @Column(length = 10000)
    private String result;

    private int score;

    private LocalDateTime date;

    public Long getId() 
    {
        return id;
    }

    public String getResumeText() 
    {
        return resumeText;
    }

    public void setResumeText(String resumeText) 
    {
        this.resumeText = resumeText;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result) 
    {
        this.result = result;
    }

    public int getScore() 
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    public LocalDateTime getDate()
    {
        return date;
    }

    public void setDate(LocalDateTime date) 
    {
        this.date = date;
    }
}