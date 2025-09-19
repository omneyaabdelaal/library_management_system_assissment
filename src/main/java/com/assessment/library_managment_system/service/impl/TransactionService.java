package com.assessment.library_managment_system.service.impl;


import com.assessment.library_managment_system.dto.TransactionDto;
import com.assessment.library_managment_system.exception.ResourceNotFoundException;
import com.assessment.library_managment_system.model.Book;
import com.assessment.library_managment_system.model.Member;
import com.assessment.library_managment_system.model.SystemUser;
import com.assessment.library_managment_system.model.Transaction;
import com.assessment.library_managment_system.repository.BookRepository;
import com.assessment.library_managment_system.repository.MemberRepository;
import com.assessment.library_managment_system.repository.SystemUserRepository;
import com.assessment.library_managment_system.repository.TransactionRepository;
import com.assessment.library_managment_system.vm.MembershipStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SystemUserRepository systemUserRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserService userService;

    public Page<TransactionDto> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable).map(this::convertToDto);
    }

    public TransactionDto getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        return convertToDto(transaction);
    }

    public Page<TransactionDto> getTransactionsByMember(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));
        return transactionRepository.findByMember(member, pageable).map(this::convertToDto);
    }

    public Page<TransactionDto> getTransactionsByBook(Long bookId, Pageable pageable) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
        return transactionRepository.findByBook(book, pageable).map(this::convertToDto);
    }

    public Page<TransactionDto> getTransactionsByStatus(Transaction.TransactionStatus status, Pageable pageable) {
        return transactionRepository.findByStatus(status, pageable).map(this::convertToDto);
    }

    public TransactionDto borrowBook(Long bookId, Long memberId, Long issuedById, String notes) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));

        SystemUser issuedBy = systemUserRepository.findById(issuedById)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + issuedById));

        // Check if book is available
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("Book is not available for borrowing");
        }

        // Check if member has active status
        if (member.getStatus() != MembershipStatus.ACTIVE) {
            throw new IllegalStateException("Member is not active");
        }

        // Check if member already has this book borrowed
        List<Transaction> existingTransactions = transactionRepository
                .findByMemberAndBookAndStatus(member, book, Transaction.TransactionStatus.BORROWED);
        if (!existingTransactions.isEmpty()) {
            throw new IllegalStateException("Member has already borrowed this book");
        }

        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setBook(book);
        transaction.setMember(member);
        transaction.setIssuedBy(issuedBy);
        transaction.setDueDate(LocalDate.now().plusDays(14)); // 2 weeks default
        transaction.setStatus(Transaction.TransactionStatus.BORROWED);
        transaction.setNotes(notes);

        // Update book availability
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToDto(savedTransaction);
    }

    public TransactionDto returnBook(Long transactionId, Long returnedById, String notes) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + transactionId));

        SystemUser returnedBy = systemUserRepository.findById(returnedById)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + returnedById));

        if (transaction.getStatus() != Transaction.TransactionStatus.BORROWED) {
            throw new IllegalStateException("Book is not currently borrowed");
        }

        // Update transaction
        transaction.setReturnedBy(returnedBy);
        transaction.setReturnDate(LocalDateTime.now());
        transaction.setStatus(Transaction.TransactionStatus.RETURNED);
        if (notes != null && !notes.isEmpty()) {
            transaction.setNotes((transaction.getNotes() != null ? transaction.getNotes() + "; " : "") + notes);
        }

        // Update book availability
        Book book = transaction.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToDto(savedTransaction);
    }

    public List<TransactionDto> getOverdueTransactions() {
        List<Transaction> overdueTransactions = transactionRepository.findOverdueTransactions(LocalDate.now());
        return overdueTransactions.stream().map(this::convertToDto).toList();
    }

    public void updateOverdueStatus() {
        List<Transaction> overdueTransactions = transactionRepository.findOverdueTransactions(LocalDate.now());
        for (Transaction transaction : overdueTransactions) {
            if (transaction.getStatus() == Transaction.TransactionStatus.BORROWED) {
                transaction.setStatus(Transaction.TransactionStatus.OVERDUE);
                transactionRepository.save(transaction);
            }
        }
    }

    private TransactionDto convertToDto(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setBook(bookService.getBookById(transaction.getBook().getId()));
        dto.setMember(memberService.getMemberById(transaction.getMember().getId()));
        dto.setIssuedBy(userService.getUserById(transaction.getIssuedBy().getId()));
        if (transaction.getReturnedBy() != null) {
            dto.setReturnedBy(userService.getUserById(transaction.getReturnedBy().getId()));
        }
        dto.setIssueDate(transaction.getIssueDate());
        dto.setDueDate(transaction.getDueDate());
        dto.setReturnDate(transaction.getReturnDate());
        dto.setStatus(transaction.getStatus().name());
        dto.setNotes(transaction.getNotes());
        return dto;
    }
}