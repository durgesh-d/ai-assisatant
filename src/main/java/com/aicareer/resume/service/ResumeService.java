package com.aicareer.resume.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aicareer.resume.model.Resume;
import com.aicareer.resume.repository.ResumeRepository;

@Service
public class ResumeService 
{

    @Autowired
    private ResumeRepository repository;

    public Resume saveResume(Resume resume)
    {
        return repository.save(resume);
    }

    public List<Resume> getAllResumes()
    {
        return repository.findAll();
    }

    public Resume getResumeById(Long id) 
    {
        return repository.findById(id).orElse(null);
    }

    public void deleteResume(Long id)
    {
        repository.deleteById(id);
    }

    public Resume updateResume(Long id, Resume resume) 
    {
        resume.setId(id);
        return repository.save(resume);
    }
}