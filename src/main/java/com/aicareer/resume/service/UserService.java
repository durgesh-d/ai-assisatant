package com.aicareer.resume.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aicareer.resume.model.User;
import com.aicareer.resume.repository.UserRepository;

@Service
public class UserService 
{

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encoder; // 🔥 NEW

    public User register(User user)
    {
        // 🔥 PASSWORD ENCODE
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public User login(String email,String password)
    {

        User user = repo.findByEmail(email);

        // 🔥 MATCH ENCODED PASSWORD
        if(user != null && encoder.matches(password, user.getPassword()))
        {
            return user;
        }

        return null;
    }
}