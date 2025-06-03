package com.amirdhesh.expenseTracker.service;

import com.amirdhesh.expenseTracker.Mapper.UserCreateMapper;
import com.amirdhesh.expenseTracker.dto.UserDTO;
import com.amirdhesh.expenseTracker.entity.User;
import com.amirdhesh.expenseTracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JWTService jwtService;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    public User register(UserDTO user) {
        User user1 = new UserCreateMapper().toEntity(user);
        user1.setPassword(encoder.encode(user1.getPassword()));
        System.out.println(user1);
        return userRepo.save(user1);
    }
    //1. Authentication Manager (authManager)
    //Delegates authentication to configured AuthenticationProvider
    //DaoAuthenticationProvider is typically the default provider for username/password auth

    //2. Authentication Process with DaoAuthenticationProvider
    //DaoAuthenticationProvider retrieves user details via UserDetailsService
    //Compares raw password with encoded password using PasswordEncoder
    //Builds Authentication object with authorities
    //Returns authenticated principal
    public String Verify(UserDTO user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        } else {
            return "fail";
        }
    }
}
