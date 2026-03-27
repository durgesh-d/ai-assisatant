package com.aicareer.resume.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aicareer.resume.model.User;
import com.aicareer.resume.security.JwtUtil;
import com.aicareer.resume.service.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController
{

    @Autowired
    private UserService service;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public User register(@RequestBody User user) 
    {
        return service.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) 
    {
        User u = service.login(user.getEmail(), user.getPassword());
        if (u != null) {
            String token = jwtUtil.generateToken(u.getEmail());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).body("Invalid Credentials");
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String header)
    {
        if (header == null || !header.startsWith("Bearer ")) 
        {
            return ResponseEntity.status(401).body("Invalid");
        }
        String token = header.substring(7);
        if (!jwtUtil.validateToken(token))
        {
            return ResponseEntity.status(401).body("Invalid");
        }
        return ResponseEntity.ok("Valid");
    }

    // GitHub OAuth2 Success
    @GetMapping("/oauth2-success")
    public void oauth2Success(HttpServletResponse response,
                              java.security.Principal principal) throws Exception 
    {
        if (principal != null) {
            String email = principal.getName();
            String token = jwtUtil.generateToken(email);
            //  Dashboard वर redirect
            response.sendRedirect("/dashboard.html?token=" + token + "&github=verified");
        } 
        else 
        {
            response.sendRedirect("/login.html?error=true");
        }
    }
}