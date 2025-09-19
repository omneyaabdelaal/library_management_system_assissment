package com.assessment.library_managment_system.service.impl;


import com.assessment.library_managment_system.dto.BookDto;
import com.assessment.library_managment_system.exception.ResourceNotFoundException;
import com.assessment.library_managment_system.model.Author;
import com.assessment.library_managment_system.model.Book;
import com.assessment.library_managment_system.model.Category;
import com.assessment.library_managment_system.model.Publisher;
import com.assessment.library_managment_system.repository.AuthorRepository;
import com.assessment.library_managment_system.repository.BookRepository;
import com.assessment.library_managment_system.repository.CategoryRepository;
import com.assessment.library_managment_system.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<BookDto> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).map(this::convertToDto);
    }

    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return convertToDto(book);
    }

    public BookDto getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ISBN: " + isbn));
        return convertToDto(book);
    }

    public Page<BookDto> searchBooksByTitle(String title, Pageable pageable) {
        return bookRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(this::convertToDto);
    }

    public Page<BookDto> searchBooksByAuthor(String authorName, Pageable pageable) {
        return bookRepository.findByAuthorNameContaining(authorName, pageable)
                .map(this::convertToDto);
    }

    public Page<BookDto> searchBooksByCategory(String categoryName, Pageable pageable) {
        return bookRepository.findByCategoryName(categoryName, pageable)
                .map(this::convertToDto);
    }

    public BookDto createBook(BookDto bookDto) {
        Book book = convertToEntity(bookDto);
        Book savedBook = bookRepository.save(book);
        return convertToDto(savedBook);
    }

    public BookDto updateBook(Long id, BookDto bookDto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        updateBookEntity(existingBook, bookDto);
        Book updatedBook = bookRepository.save(existingBook);
        return convertToDto(updatedBook);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    public BookDto updateBookCopies(Long id, Integer totalCopies, Integer availableCopies) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        book.setTotalCopies(totalCopies);
        book.setAvailableCopies(availableCopies);

        Book updatedBook = bookRepository.save(book);
        return convertToDto(updatedBook);
    }

    private BookDto convertToDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setEdition(book.getEdition());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setLanguage(book.getLanguage());
        dto.setSummary(book.getSummary());
        dto.setCoverImageUrl(book.getCoverImageUrl());
        dto.setTotalCopies(book.getTotalCopies());
        dto.setAvailableCopies(book.getAvailableCopies());

        // Convert authors
        if (book.getAuthors() != null) {
            Set<BookDto.AuthorDto> authorDtos = book.getAuthors().stream()
                    .map(author -> {
                        BookDto.AuthorDto authorDto = new BookDto.AuthorDto();
                        authorDto.setId(author.getId());
                        authorDto.setFirstName(author.getFirstName());
                        authorDto.setLastName(author.getLastName());
                        authorDto.setBiography(author.getBiography());
                        return authorDto;
                    }).collect(Collectors.toSet());
            dto.setAuthors(authorDtos);
        }

        // Convert publisher
        if (book.getPublisher() != null) {
            BookDto.PublisherDto publisherDto = new BookDto.PublisherDto();
            publisherDto.setId(book.getPublisher().getId());
            publisherDto.setName(book.getPublisher().getName());
            publisherDto.setAddress(book.getPublisher().getAddress());
            publisherDto.setContactEmail(book.getPublisher().getContactEmail());
            publisherDto.setContactPhone(book.getPublisher().getContactPhone());
            dto.setPublisher(publisherDto);
        }

        // Convert categories
        if (book.getCategories() != null) {
            Set<BookDto.CategoryDto> categoryDtos = book.getCategories().stream()
                    .map(category -> {
                        BookDto.CategoryDto categoryDto = new BookDto.CategoryDto();
                        categoryDto.setId(category.getId());
                        categoryDto.setName(category.getName());
                        categoryDto.setDescription(category.getDescription());
                        categoryDto.setParentId(category.getParent() != null ? category.getParent().getId() : null);
                        return categoryDto;
                    }).collect(Collectors.toSet());
            dto.setCategories(categoryDtos);
        }

        return dto;
    }

    private Book convertToEntity(BookDto dto) {
        Book book = new Book();
        updateBookEntity(book, dto);
        return book;
    }

    private void updateBookEntity(Book book, BookDto dto) {
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setEdition(dto.getEdition());
        book.setPublicationYear(dto.getPublicationYear());
        book.setLanguage(dto.getLanguage());
        book.setSummary(dto.getSummary());
        book.setCoverImageUrl(dto.getCoverImageUrl());
        book.setTotalCopies(dto.getTotalCopies());
        book.setAvailableCopies(dto.getAvailableCopies());

        // Handle authors
        if (dto.getAuthors() != null) {
            Set<Author> authors = new HashSet<>();
            for (BookDto.AuthorDto authorDto : dto.getAuthors()) {
                Author author;
                if (authorDto.getId() != null) {
                    author = authorRepository.findById(authorDto.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Author not found"));
                } else {
                    author = new Author();
                    author.setFirstName(authorDto.getFirstName());
                    author.setLastName(authorDto.getLastName());
                    author.setBiography(authorDto.getBiography());
                }
                authors.add(author);
            }
            book.setAuthors(authors);
        }

        // Handle publisher
        if (dto.getPublisher() != null) {
            Publisher publisher;
            if (dto.getPublisher().getId() != null) {
                publisher = publisherRepository.findById(dto.getPublisher().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Publisher not found"));
            } else {
                publisher = new Publisher();
                publisher.setName(dto.getPublisher().getName());
                publisher.setAddress(dto.getPublisher().getAddress());
                publisher.setContactEmail(dto.getPublisher().getContactEmail());
                publisher.setContactPhone(dto.getPublisher().getContactPhone());
            }
            book.setPublisher(publisher);
        }

        // Handle categories
        if (dto.getCategories() != null) {
            Set<Category> categories = new HashSet<>();
            for (BookDto.CategoryDto categoryDto : dto.getCategories()) {
                Category category;
                if (categoryDto.getId() != null) {
                    category = categoryRepository.findById(categoryDto.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
                } else {
                    category = new Category();
                    category.setName(categoryDto.getName());
                    category.setDescription(categoryDto.getDescription());
                    if (categoryDto.getParentId() != null) {
                        Category parent = categoryRepository.findById(categoryDto.getParentId())
                                .orElseThrow(() -> new ResourceNotFoundException("Parent category not found"));
                        category.setParent(parent);
                    }
                }
                categories.add(category);
            }
            book.setCategories(categories);
        }
    }
}
