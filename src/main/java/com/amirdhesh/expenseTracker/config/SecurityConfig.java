package com.amirdhesh.expenseTracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request -> request.requestMatchers("/api/register", "/api/login")
                        .permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //The default UsernamePasswordAuthenticationFilter might process the request first.
                //But it will likely not find any form data (because you’re using JWT, not form login).
                //So it will do nothing or fail, and the request will proceed as unauthenticated.
                //Your JWT filter runs too late to be useful.
                //Authenticate the request (by setting SecurityContextHolder.getContext().setAuthentication(...))
                //Before 🔐 any other Spring Security filter that checks for authentication.

                //Why addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)?
                //Ensures JWT is validated before Spring tries to authenticate or authorize anything.
                //Makes sure SecurityContext is properly set up for the rest of the filters.
                //Guarantees that Spring Security treats the request as authenticated.
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    //DaoauthernticationProvider implements AuthenticationProvider
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(10));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
        //Register AuthrnticationManager in Spring Beans.
    }
}
