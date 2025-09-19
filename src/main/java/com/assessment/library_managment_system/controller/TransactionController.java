package com.assessment.library_managment_system.controller;


import com.assessment.library_managment_system.dto.TransactionDto;
import com.assessment.library_managment_system.model.Transaction;
import com.assessment.library_managment_system.service.impl.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    @PreAuthorize("hasRole('STAFF') or hasRole('LIBRARIAN') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<Page<TransactionDto>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "issueDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<TransactionDto> transactions = transactionService.getAllTransactions(pageable);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('STAFF') or hasRole('LIBRARIAN') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable Long id) {
        TransactionDto transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/member/{memberId}")
    @PreAuthorize("hasRole('STAFF') or hasRole('LIBRARIAN') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<Page<TransactionDto>> getTransactionsByMember(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("issueDate").descending());
        Page<TransactionDto> transactions = transactionService.getTransactionsByMember(memberId, pageable);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/book/{bookId}")
    @PreAuthorize("hasRole('STAFF') or hasRole('LIBRARIAN') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<Page<TransactionDto>> getTransactionsByBook(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("issueDate").descending());
        Page<TransactionDto> transactions = transactionService.getTransactionsByBook(bookId, pageable);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('STAFF') or hasRole('LIBRARIAN') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<Page<TransactionDto>> getTransactionsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("issueDate").descending());
        Transaction.TransactionStatus transactionStatus = Transaction.TransactionStatus.valueOf(status.toUpperCase());
        Page<TransactionDto> transactions = transactionService.getTransactionsByStatus(transactionStatus, pageable);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/borrow")
    @PreAuthorize("hasRole('STAFF') or hasRole('LIBRARIAN') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<TransactionDto> borrowBook(
            @RequestParam Long bookId,
            @RequestParam Long memberId,
            @RequestParam Long issuedById,
            @RequestParam(required = false) String notes) {

        TransactionDto transaction = transactionService.borrowBook(bookId, memberId, issuedById, notes);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @PutMapping("/{id}/return")
    @PreAuthorize("hasRole('STAFF') or hasRole('LIBRARIAN') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<TransactionDto> returnBook(
            @PathVariable Long id,
            @RequestParam Long returnedById,
            @RequestParam(required = false) String notes) {

        TransactionDto transaction = transactionService.returnBook(id, returnedById, notes);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasRole('STAFF') or hasRole('LIBRARIAN') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<List<TransactionDto>> getOverdueTransactions() {
        List<TransactionDto> overdueTransactions = transactionService.getOverdueTransactions();
        return ResponseEntity.ok(overdueTransactions);
    }

    @PutMapping("/update-overdue-status")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> updateOverdueStatus() {
        transactionService.updateOverdueStatus();
        return ResponseEntity.ok().build();
    }
}