-- Create roles
INSERT INTO roles (id, name, description) VALUES 
(1, 'ROLE_ADMINISTRATOR', 'Full system access with all permissions');
INSERT INTO roles (id, name, description) VALUES 
(2, 'ROLE_LIBRARIAN', 'Can manage books, members, and transactions');
INSERT INTO roles (id, name, description) VALUES 
(3, 'ROLE_STAFF', 'Can issue and return books, view members');

-- Create default admin user (password: admin123)
INSERT INTO system_users (id, username, password, email, first_name, last_name, enabled, created_at, updated_at) 
VALUES (1, 'admin', '$2a$10$yBEfXNzq3GJdNjNg4n4pRuVSRRDBp6XZJ6QGY4mfkT4xKGnGw2qya', 
        'admin@library.com', 'System', 'Administrator', 1, SYSDATE, SYSDATE);

-- Assign admin role
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);

-- Create sample librarian (password: librarian123)
INSERT INTO system_users (id, username, password, email, first_name, last_name, enabled, created_at, updated_at) 
VALUES (2, 'librarian', '$2a$10$DqW4jyX8n2pRuJY5RuLGROwgxKxQGnWdKYEp5z8vLvJ2kHfXpMd0G', 
        'librarian@library.com', 'Jane', 'Smith', 1, SYSDATE, SYSDATE);

-- Assign librarian role
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);

-- Create sample staff (password: staff123)
INSERT INTO system_users (id, username, password, email, first_name, last_name, enabled, created_at, updated_at) 
VALUES (3, 'staff', '$2a$10$L2mHfQjNbLXfpWRGx5jNbONVJz8qHvYfKNx4tUeR8hJzgKpDvMqSy', 
        'staff@library.com', 'John', 'Doe', 1, SYSDATE, SYSDATE);

-- Assign staff role
INSERT INTO user_roles (user_id, role_id) VALUES (3, 3);

-- Create sample publishers
INSERT INTO publishers (id, name, address, contact_email, contact_phone) VALUES 
(1, 'Penguin Random House', '1745 Broadway, New York, NY 10019', 'contact@penguinrandomhouse.com', '+1-212-782-9000');
INSERT INTO publishers (id, name, address, contact_email, contact_phone) VALUES 
(2, 'HarperCollins', '195 Broadway, New York, NY 10007', 'info@harpercollins.com', '+1-212-207-7000');
INSERT INTO publishers (id, name, address, contact_email, contact_phone) VALUES 
(3, 'Simon & Schuster', '1230 Avenue of the Americas, New York, NY 10020', 'contact@simonandschuster.com', '+1-212-698-7000');
INSERT INTO publishers (id, name, address, contact_email, contact_phone) VALUES 
(4, 'Macmillan Publishers', '120 Broadway, New York, NY 10271', 'info@macmillan.com', '+1-646-307-5151');

-- Create sample categories
INSERT INTO categories (id, name, description, parent_id) VALUES (1, 'Fiction', 'Fictional literature', NULL);
INSERT INTO categories (id, name, description, parent_id) VALUES (2, 'Non-Fiction', 'Non-fictional literature', NULL);
INSERT INTO categories (id, name, description, parent_id) VALUES (3, 'Science', 'Scientific books and research', NULL);
INSERT INTO categories (id, name, description, parent_id) VALUES (4, 'Technology', 'Technology and computer science', NULL);
INSERT INTO categories (id, name, description, parent_id) VALUES (5, 'Mystery', 'Mystery and thriller novels', 1);
INSERT INTO categories (id, name, description, parent_id) VALUES (6, 'Romance', 'Romance novels', 1);
INSERT INTO categories (id, name, description, parent_id) VALUES (7, 'Fantasy', 'Fantasy and magical realism', 1);
INSERT INTO categories (id, name, description, parent_id) VALUES (8, 'Biography', 'Biographies and memoirs', 2);
INSERT INTO categories (id, name, description, parent_id) VALUES (9, 'History', 'Historical books', 2);
INSERT INTO categories (id, name, description, parent_id) VALUES (10, 'Self-Help', 'Self-improvement books', 2);
INSERT INTO categories (id, name, description, parent_id) VALUES (11, 'Physics', 'Physics books', 3);
INSERT INTO categories (id, name, description, parent_id) VALUES (12, 'Chemistry', 'Chemistry books', 3);
INSERT INTO categories (id, name, description, parent_id) VALUES (13, 'Biology', 'Biology books', 3);
INSERT INTO categories (id, name, description, parent_id) VALUES (14, 'Programming', 'Programming and software development', 4);
INSERT INTO categories (id, name, description, parent_id) VALUES (15, 'Web Development', 'Web development books', 4);

-- Create sample authors
INSERT INTO authors (id, first_name, last_name, biography) VALUES (1, 'Agatha', 'Christie', 'British crime writer known for detective novels featuring Hercule Poirot and Miss Marple');
INSERT INTO authors (id, first_name, last_name, biography) VALUES (2, 'J.K.', 'Rowling', 'British author best known for the Harry Potter fantasy series');
INSERT INTO authors (id, first_name, last_name, biography) VALUES (3, 'Stephen', 'King', 'American author of horror, supernatural fiction, suspense, and fantasy novels');
INSERT INTO authors (id, first_name, last_name, biography) VALUES (4, 'Jane', 'Austen', 'English novelist known primarily for her six major novels');
INSERT INTO authors (id, first_name, last_name, biography) VALUES (5, 'George', 'Orwell', 'English novelist and essayist, journalist and critic');
INSERT INTO authors (id, first_name, last_name, biography) VALUES (6, 'Harper', 'Lee', 'American novelist widely known for To Kill a Mockingbird');
INSERT INTO authors (id, first_name, last_name, biography) VALUES (7, 'F. Scott', 'Fitzgerald', 'American novelist and short story writer');
INSERT INTO authors (id, first_name, last_name, biography) VALUES (8, 'Ernest', 'Hemingway', 'American novelist, short-story writer, and journalist');
INSERT INTO authors (id, first_name, last_name, biography) VALUES (9, 'Mark', 'Twain', 'American writer, humorist, entrepreneur, publisher, and lecturer');
INSERT INTO authors (id, first_name, last_name, biography) VALUES (10, 'Charles', 'Dickens', 'English writer and social critic');

-- Create sample books
INSERT INTO book VALUES 
(2, 'Harry Potter and the Philosopher''s Stone', '978-0-7475-3269-9', '1st', 1997, 'English', 'The first book in the Harry Potter series', 'https://example.com/harry-potter-1.jpg', 10, 8, 2, SYSDATE, SYSDATE);
INSERT INTO book VALUES 
(3, 'The Shining', '978-0-385-12167-5', '1st', 1977, 'English', 'A horror novel about a family in an isolated hotel', 'https://example.com/the-shining.jpg', 3, 2, 3, SYSDATE, SYSDATE);
INSERT INTO book VALUES 
(4, 'Pride and Prejudice', '978-0-14-143951-8', '2nd', 1813, 'English', 'A romantic novel about Elizabeth Bennet and Mr. Darcy', 'https://example.com/pride-prejudice.jpg', 7, 7, 1, SYSDATE, SYSDATE);
INSERT INTO book VALUES 
(5, '1984', '978-0-452-28423-4', '1st', 1949, 'English', 'A dystopian social science fiction novel', 'https://example.com/1984.jpg', 6, 4, 4, SYSDATE, SYSDATE);
INSERT INTO book VALUES 
(6, 'To Kill a Mockingbird', '978-0-06-112008-4', '1st', 1960, 'English', 'A novel about racial injustice in the American South', 'https://example.com/to-kill-mockingbird.jpg', 8, 6, 2, SYSDATE, SYSDATE);
INSERT INTO book VALUES 
(7, 'The Great Gatsby', '978-0-7432-7356-5', '1st', 1925, 'English', 'A classic American novel about the Jazz Age', 'https://example.com/great-gatsby.jpg', 4, 4, 3, SYSDATE, SYSDATE);
INSERT INTO book VALUES 
(8, 'The Old Man and the Sea', '978-0-684-80122-3', '1st', 1952, 'English', 'A short novel about an aging Cuban fisherman', 'https://example.com/old-man-sea.jpg', 5, 3, 1, SYSDATE, SYSDATE);
INSERT INTO book VALUES 
(9, 'Adventures of Huckleberry Finn', '978-0-486-28061-5', '1st', 1884, 'English', 'A novel about a boy''s journey down the Mississippi River', 'https://example.com/huck-finn.jpg', 6, 5, 2, SYSDATE, SYSDATE);
INSERT INTO books VALUES 
(10, 'A Tale of Two Cities', '978-0-14-143960-0', '1st', 1859, 'English', 'A historical novel set in London and Paris before and during the French Revolution', 'https://example.com/tale-two-cities.jpg', 4, 4, 4, SYSDATE, SYSDATE);

-- Associate books with authors
INSERT INTO book_authors (book_id, author_id) VALUES (1, 1);
INSERT INTO book_authors VALUES (2, 2);
INSERT INTO book_authors VALUES (3, 3);
INSERT INTO book_authors VALUES (4, 4);
INSERT INTO book_authors VALUES (5, 5);
INSERT INTO book_authors VALUES (6, 6);
INSERT INTO book_authors VALUES (7, 7);
INSERT INTO book_authors VALUES (8, 8);
INSERT INTO book_authors VALUES (9, 9);
INSERT INTO book_authors VALUES (10, 10);

-- Associate books with categories
INSERT INTO book_categories (book_id, category_id) VALUES (1, 5);
INSERT INTO book_categories VALUES (2, 7);
INSERT INTO book_categories VALUES (3, 1);
INSERT INTO book_categories VALUES (4, 6);
INSERT INTO book_categories VALUES (5, 1);
INSERT INTO book_categories VALUES (6, 1);
INSERT INTO book_categories VALUES (7, 1);
INSERT INTO book_categories VALUES (8, 1);
INSERT INTO book_categories VALUES (9, 1);
INSERT INTO book_categories VALUES (10, 9);

-- Create sample members
INSERT INTO members (id, member_id, first_name, last_name, email, phone, address, date_of_birth, status, membership_date) VALUES 
(1, 'LIB1001', 'Alice', 'Johnson', 'alice.johnson@email.com', '+1-555-0101', '123 Main St, Springfield, IL', TO_DATE('1985-03-15','YYYY-MM-DD'), 'ACTIVE', SYSDATE);
INSERT INTO members VALUES 
(2, 'LIB1002', 'Bob', 'Smith', 'bob.smith@email.com', '+1-555-0102', '456 Oak Ave, Springfield, IL', TO_DATE('1990-07-22','YYYY-MM-DD'), 'ACTIVE', SYSDATE);
INSERT INTO members VALUES 
(3, 'LIB1003', 'Carol', 'Davis', 'carol.davis@email.com', '+1-555-0103', '789 Pine St, Springfield, IL', TO_DATE('1988-11-08','YYYY-MM-DD'), 'ACTIVE', SYSDATE);
INSERT INTO members VALUES 
(4, 'LIB1004', 'David', 'Wilson', 'david.wilson@email.com', '+1-555-0104', '321 Elm St, Springfield, IL', TO_DATE('1992-01-30','YYYY-MM-DD'), 'SUSPENDED', SYSDATE);
INSERT INTO members VALUES 
(5, 'LIB1005', 'Eva', 'Brown', 'eva.brown@email.com', '+1-555-0105', '654 Maple Ave, Springfield, IL', TO_DATE('1987-05-12','YYYY-MM-DD'), 'ACTIVE', SYSDATE);

-- Create sample transactions
INSERT INTO transactions (id, book_id, member_id, issued_by, issue_date, due_date, status, notes) VALUES 
(1, 2, 1, 2, SYSDATE - 5, TRUNC(SYSDATE) + 9, 'BORROWED', 'Regular borrowing');
INSERT INTO transactions VALUES 
(2, 5, 2, 2, SYSDATE - 10, TRUNC(SYSDATE) + 4, 'BORROWED', 'Member requested this book');
INSERT INTO transactions VALUES 
(3, 3, 3, 3, SYSDATE - 3, TRUNC(SYSDATE) + 11, 'BORROWED', 'First time borrower');
INSERT INTO transactions VALUES 
(4, 6, 5, 2, SYSDATE - 20, SYSDATE - 6, 'RETURNED', 'Returned on time');
INSERT INTO transactions VALUES 
(5, 8, 4, 3, SYSDATE - 8, TRUNC(SYSDATE) + 6, 'BORROWED', 'Popular book request');

-- Update returned transaction
UPDATE transactions 
SET returned_by = 2, return_date = SYSDATE - 6 
WHERE id = 4;


