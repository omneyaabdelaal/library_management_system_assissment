package com.assessment.library_managment_system.service.impl;

import com.assessment.library_managment_system.dto.LoginRequest;
import com.assessment.library_managment_system.management.security.JwtTokenProvider;
import com.assessment.library_managment_system.model.SystemUser;
import com.assessment.library_managment_system.repository.SystemUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private SystemUserRepository systemUserRepository;

    @Autowired
    private UserActivityService userActivityService;

    public String authenticateUser(LoginRequest loginRequest, String ipAddress) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        // Log login activity
        SystemUser user = systemUserRepository.findByUsername(loginRequest.getUsername()).orElse(null);
        if (user != null) {
            userActivityService.logActivity(user.getId(), "LOGIN", "User logged in", ipAddress);
        }

        return jwt;
    }
}
