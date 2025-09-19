package com.assessment.library_managment_system.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserDto {
        private Long id;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private boolean enabled;
        private Set<String> roles;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
}
