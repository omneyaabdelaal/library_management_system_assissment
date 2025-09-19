package com.assessment.library_managment_system.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TransactionDto {
    private Long id;
    private BookDto book;
    private MemberDto member;
    private UserDto issuedBy;
    private UserDto returnedBy;
    private LocalDateTime issueDate;
    private LocalDate dueDate;
    private LocalDateTime returnDate;
    private String status;
    private String notes;
}