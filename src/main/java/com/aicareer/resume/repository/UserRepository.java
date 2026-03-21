package com.aicareer.resume.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.aicareer.resume.model.User;

public interface UserRepository extends JpaRepository<User,Long>
{

    User findByEmail(String email);

}