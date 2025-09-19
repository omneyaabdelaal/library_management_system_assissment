package com.assessment.library_managment_system.repository;



import com.assessment.library_managment_system.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);

    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE " +
            "LOWER(CONCAT(a.firstName, ' ', a.lastName)) LIKE LOWER(CONCAT('%', :authorName, '%'))")
    Page<Book> findByAuthorNameContaining(@Param("authorName") String authorName, Pageable pageable);

    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.name = :categoryName")
    Page<Book> findByCategoryName(@Param("categoryName") String categoryName, Pageable pageable);

    List<Book> findByAvailableCopiesGreaterThan(Integer minCopies);

    @Query("SELECT b FROM Book b WHERE b.availableCopies = 0")
    List<Book> findOutOfStockBooks();
}