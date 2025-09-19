package com.assessment.library_managment_system.dto;

import com.assessment.library_managment_system.vm.MembershipStatus;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MemberDto {
        private Long id;
        private String memberId;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String address;
        private LocalDate dateOfBirth;
        private MembershipStatus status;
        private LocalDateTime membershipDate;
}