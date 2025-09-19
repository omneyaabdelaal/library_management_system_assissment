package com.assessment.library_managment_system.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}