package com.assessment.library_managment_system.repository;


import com.assessment.library_managment_system.model.Book;
import com.assessment.library_managment_system.model.Member;
import com.assessment.library_managment_system.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByMember(Member member, Pageable pageable);
    Page<Transaction> findByBook(Book book, Pageable pageable);
    Page<Transaction> findByStatus(Transaction.TransactionStatus status, Pageable pageable);

    List<Transaction> findByMemberAndBookAndStatus(Member member, Book book,
                                                   Transaction.TransactionStatus status);

    @Query("SELECT t FROM Transaction t WHERE t.status = 'BORROWED' AND t.dueDate < :currentDate")
    List<Transaction> findOverdueTransactions(LocalDate currentDate);

    long countByMemberAndStatus(Member member, Transaction.TransactionStatus status);
}