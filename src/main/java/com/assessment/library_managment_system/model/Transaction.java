package com.assessment.library_managment_system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "issued_by", nullable = false)
    private SystemUser issuedBy;

    @ManyToOne
    @JoinColumn(name = "returned_by")
    private SystemUser returnedBy;

    @Column(name = "issue_date")
    @CreatedDate
    private LocalDateTime issueDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.BORROWED;

    private String notes;

    public enum TransactionStatus {
        BORROWED, RETURNED, OVERDUE
    }
}