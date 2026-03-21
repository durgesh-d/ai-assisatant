package com.aicareer.resume.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.aicareer.resume.model.Resume;

public interface ResumeRepository extends JpaRepository<Resume, Long>
{

}