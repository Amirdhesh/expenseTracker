package com.amirdhesh.expenseTracker.repository;

import com.amirdhesh.expenseTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByusername(String username);
}
