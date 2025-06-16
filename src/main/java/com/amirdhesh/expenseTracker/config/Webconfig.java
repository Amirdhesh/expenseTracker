package com.amirdhesh.expenseTracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class Webconfig implements WebMvcConfigurer {
    @Autowired
    private Middleware  middleware;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(middleware);
    }

}
