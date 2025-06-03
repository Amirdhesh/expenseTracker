package com.amirdhesh.expenseTracker.config;

import com.amirdhesh.expenseTracker.service.JWTService;
import com.amirdhesh.expenseTracker.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    private JWTService jwtService;
    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //It gets the Authorization header.
        //Checks if it starts with "Bearer " (standard format).
        //Extracts the JWT token by removing "Bearer " prefix.
        //Uses jwtService to extract the username from the token payload.
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUserName(token);
        }
        //Username was successfully extracted.
        //No authentication is currently set in the SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //Loads the user from the database (or user service).
            //Validates the token (e.g., expiry, signature, subject match).
            UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);
            if (jwtService.validateToken(token, userDetails)) {
                //Creates a Spring Security authentication token.
                //Attaches additional request details (e.g., IP address, session info).
                //Sets the token in the current security context so downstream code can see the user is authenticated.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);

    }
}

//Flow:
//Intercept request.
//Check Authorization header.
//Extract JWT token.
//Extract username from token.
//If the user is not already authenticated:
//Load user from DB.
//Validate the token.
//Set authentication in SecurityContext.
// [if dont set the authenticated user into the SecurityContext, Request continues to your secured endpoint, Spring Security checks the SecurityContext, Access Denied → Spring Security returns 401 Unauthorized or 403 Forbidden]
//Continue with the request.

//if Invalid JWT Token is given:
//[No authentication is set in the SecurityContext.
//The request proceeds to the next filter (or controller) with an unauthenticated user:]


