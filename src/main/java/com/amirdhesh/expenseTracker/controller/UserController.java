package com.amirdhesh.expenseTracker.controller;

import com.amirdhesh.expenseTracker.dto.UserDTO;
import com.amirdhesh.expenseTracker.entity.User;
import com.amirdhesh.expenseTracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO user) {
        User user1 = userService.register(user);
        System.out.println(user1);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDTO user) {
        return userService.Verify(user);
    }
}
