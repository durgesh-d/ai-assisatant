package com.aicareer.resume.repository;

import com.aicareer.resume.model.ResumeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResumeHistoryRepository extends JpaRepository<ResumeHistory, Long> {
    List<ResumeHistory> findByUserEmailOrderByCreatedAtDesc(String userEmail);
    void deleteById(Long id);
}