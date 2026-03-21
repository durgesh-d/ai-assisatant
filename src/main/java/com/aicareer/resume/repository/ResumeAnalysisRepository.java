package com.aicareer.resume.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.aicareer.resume.model.ResumeAnalysis;

public interface ResumeAnalysisRepository extends JpaRepository<ResumeAnalysis, Long>
{

}