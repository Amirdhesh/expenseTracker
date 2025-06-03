package com.amirdhesh.expenseTracker.Mapper;

import com.amirdhesh.expenseTracker.dto.UserDTO;
import com.amirdhesh.expenseTracker.entity.User;

public class UserCreateMapper implements ToEntityMapper<UserDTO, User> {
    public User toEntity(UserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        return user;
    }
}
