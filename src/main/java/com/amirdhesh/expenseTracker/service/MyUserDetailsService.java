package com.amirdhesh.expenseTracker.service;

import com.amirdhesh.expenseTracker.entity.User;
import com.amirdhesh.expenseTracker.entity.UserPrinciple;
import com.amirdhesh.expenseTracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//This is used by DaoAuthenticstion Provider to load user from Database
@Service
public class MyUserDetailsService implements UserDetailsService { //UserDetailsService is an interface
    @Autowired
    UserRepository userrepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userrepo.findByusername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User Not Found.");
        }
        return new UserPrinciple(user); //UserPrinciple is an implementation of UserDetails interface
    }
}
