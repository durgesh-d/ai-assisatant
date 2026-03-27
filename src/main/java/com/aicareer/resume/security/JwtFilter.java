package com.aicareer.resume.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter
{

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain)
            throws ServletException, IOException
    {

        String path = request.getRequestURI();

       // public path
        if (isPublicPath(path))
        {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        // no token
        if (header == null || !header.startsWith("Bearer "))
        {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing Token");
            return;
        }

        String token = header.substring(7);

        // Invalid Token
        if (!jwtUtil.validateToken(token)) 
        {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid Token");
            return;
        }

        // Valid Token → Continue
        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) 
    {
        return path.equals("/") ||

               // HTML PAGES
               path.equals("/login.html") ||
               path.equals("/register.html") ||
               path.equals("/dashboard.html") ||  

               // STATIC FILES
               path.startsWith("/css/") ||
               path.startsWith("/js/") ||
               path.startsWith("/images/") ||

               // AUTH APIs
               path.startsWith("/auth/") ||

               //  OAUTH2
               path.startsWith("/oauth2/") ||
               path.startsWith("/login/oauth2/") ||

               //  BROWSER EXTRA REQUESTS
               path.contains("favicon");
    }
}