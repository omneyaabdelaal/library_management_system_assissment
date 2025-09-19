# Library Management System

A comprehensive Library Management System built with Java, Spring Boot, and MySQL that provides complete functionality for managing books, members, transactions, and system users with role-based access control.

## Features

### Core Features
- **Book Management**: Complete CRUD operations with extended metadata (authors, publishers, categories, ISBN, editions)
- **Member Management**: Full member lifecycle management with status tracking
- **Transaction Management**: Book borrowing and returning with due date tracking
- **User Management**: System users with role-based access control
- **Authentication & Authorization**: JWT-based security with three user roles
- **Activity Logging**: Comprehensive user activity tracking
- **Search Functionality**: Advanced search for books by title, author, category, and ISBN

### Advanced Features
- **Hierarchical Categories**: Support for parent-child category relationships
- **Multiple Authors per Book**: Books can have multiple authors
- **Overdue Tracking**: Automatic identification and management of overdue books
- **Role-based Permissions**: Three distinct roles with different access levels
- **RESTful API**: Complete REST API with proper HTTP methods and status codes
- **Exception Handling**: Global exception handling with proper error responses

## Technology Stack

- **Backend**: Java 17, Spring Boot 3.5.5
- **Security**: Spring Security with JWT authentication
- **Database**: oracle dataase
- **ORM**: Spring Data JPA with Hibernate
- **Build Tool**: Maven
- **Additional Libraries**: Lombok, JJWT

## Architecture

### Database Schema
The system uses a well-designed relational database schema with the following entities:
- **Books**: Extended metadata with relationships to authors, publishers, and categories
- **Authors**: Support for multiple authors per book
- **Categories**: Hierarchical category structure
- **Publishers**: Publisher information and contact details
- **Members**: Borrower information with membership status
- **System Users**: Staff, librarians, and administrators
- **Roles**: Role-based access control
- **Transactions**: Borrowing and return records
- **User Activities**: Audit trail for user actions

### API Design
RESTful API endpoints organized by resource:
- `/api/books` - Book management
- `/api/members` - Member management
- `/api/users` - System user management
- `/api/transactions` - Borrowing and return operations
- `/api/auth` - Authentication

## User Roles & Permissions

### Administrator (ROLE_ADMINISTRATOR)
- Full system access
- Manage all users, books, members, and transactions
- Delete operations on all resources
- View all activity logs

### Librarian (ROLE_LIBRARIAN)
- Manage books and their metadata
- Manage members and their status
- Process all transactions (borrow/return)
- Update book inventory
- Cannot manage system users

### Staff (ROLE_STAFF)
- View books and members
- Process basic transactions (borrow/return)
- Cannot modify book or member information
- Limited access to management functions

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login

### Books
- `GET /api/books` - Get all books (paginated)
- `GET /api/books/{id}` - Get book by ID
- `GET /api/books/isbn/{isbn}` - Get book by ISBN
- `GET /api/books/search/title?title={title}` - Search books by title
- `GET /api/books/search/author?author={author}` - Search books by author
- `GET /api/books/search/category?category={category}` - Search books by category
- `POST /api/books` - Create new book (Librarian+)
- `PUT /api/books/{id}` - Update book (Librarian+)
- `DELETE /api/books/{id}` - Delete book (Admin only)
- `PUT /api/books/{id}/copies` - Update book copies (Librarian+)

### Members
- `GET /api/members` - Get all members (Staff+)
- `GET /api/members/{id}` - Get member by ID (Staff+)
- `GET /api/members/member-id/{memberId}` - Get member by member ID (Staff+)
- `GET /api/members/search?name={name}` - Search members by name (Staff+)
- `GET /api/members/status/{status}` - Get members by status (Staff+)
- `POST /api/members` - Create new member (Staff+)
- `PUT /api/members/{id}` - Update member (Staff+)
- `DELETE /api/members/{id}` - Delete member (Librarian+)
- `PUT /api/members/{id}/status` - Update member status (Librarian+)

### Transactions
- `GET /api/transactions` - Get all transactions (Staff+)
- `GET /api/transactions/{id}` - Get transaction by ID (Staff+)
- `GET /api/transactions/member/{memberId}` - Get member's transactions (Staff+)
- `GET /api/transactions/book/{bookId}` - Get book's transaction history (Staff+)
- `GET /api/transactions/status/{status}` - Get transactions by status (Staff+)
- `POST /api/transactions/borrow` - Borrow a book (Staff+)
- `PUT /api/transactions/{id}/return` - Return a book (Staff+)
- `GET /api/transactions/overdue` - Get overdue transactions (Staff+)
- `PUT /api/transactions/update-overdue-status` - Update overdue status (Librarian+)

### Users
- `GET /api/users` - Get all users (Admin only)
- `GET /api/users/{id}` - Get user by ID (Admin/Self)
- `POST /api/users` - Create new user (Admin only)
- `PUT /api/users/{id}` - Update user (Admin/Self)
- `DELETE /api/users/{id}` - Delete user (Admin only)
- `PUT /api/users/{id}/password` - Update password (Admin/Self)
- `PUT /api/users/{id}/roles` - Update user roles (Admin only)
- `PUT /api/users/{id}/toggle-status` - Enable/disable user (Admin only)
- `GET /api/users/{id}/activities` - Get user activities (Admin/Self)

## Getting Started

### Prerequisites
- Java 17 or later
- Maven 3.6+
- oracle 19.22.0.0

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd library-management-system
   ```

2. **Setup Oracle Database**
   ```sql
   CREATE DATABASE library_management;
   CREATE USER 'library_user'@'localhost' IDENTIFIED BY 'password';
   GRANT ALL PRIVILEGES ON library_management.* TO 'library_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **Configure Database Connection**
   Update `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/library_management
   spring.datasource.username=library_user
   spring.datasource.password=your_password
   ```

4. **Build and Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

5. **Access the Application**
   - Base URL: `http://localhost:8080`
   - API Documentation: Available through endpoints

### Default Users
The system comes with pre-configured users:

| Username  | Password     | Role          |
|-----------|--------------|---------------|
| admin     | admin123     | Administrator |
| librarian | librarian123 | Librarian     |
| staff     | staff123     | Staff         |

### Sample API Usage

1. **Login**
   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"admin","password":"admin123"}'
   ```

2. **Get Books** (using the token from login)
   ```bash
   curl -X GET http://localhost:8080/api/books \
     -H "Authorization: Bearer <your-jwt-token>"
   ```

3. **Search Books by Title**
   ```bash
   curl -X GET "http://localhost:8080/api/books/search/title?title=Harry Potter" \
     -H "Authorization: Bearer <your-jwt-token>"
   ```

4. **Borrow a Book**
   ```bash
   curl -X POST "http://localhost:8080/api/transactions/borrow?bookId=1&memberId=1&issuedById=2" \
     -H "Authorization: Bearer <your-jwt-token>"
   ```

## Design Decisions

### Database Design
- **Normalized Schema**: Follows 3NF to reduce redundancy
- **Hierarchical Categories**: Self-referencing table for category taxonomy
- **Many-to-Many Relationships**: Books can have multiple authors and categories
- **Audit Fields**: Created/updated timestamps for tracking
- **Soft References**: Foreign keys maintain referential integrity

### Security
- **JWT Authentication**: Stateless authentication suitable for REST APIs
- **Role-Based Access Control**: Method-level security with Spring Security
- **Password Encryption**: BCrypt hashing for secure password storage
- **Activity Logging**: Comprehensive audit trail for user actions

### API Design
- **RESTful Principles**: Standard HTTP methods and status codes
- **Pagination**: Large datasets are paginated for performance
- **Search Functionality**: Multiple search criteria for books and members
- **Error Handling**: Consistent error responses with meaningful messages

### Performance Considerations
- **Lazy Loading**: JPA lazy loading for related entities
- **Indexing**: Database indexes on frequently queried fields
- **Pagination**: Prevents large dataset loading issues
- **Connection Pooling**: Efficient database connection management


