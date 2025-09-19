package com.assessment.library_managment_system.vm.security;

import jakarta.validation.constraints.*;

public record AuthRequest(
        @NotNull(message = "Email is required")
        @Size(min = 1, max = 255, message = "Email must be between 1 and 255 characters")
        @Email(message = "Invalid email format")
        String email,
        @NotNull(message = "Password is required")
        @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
        String password
) {}
