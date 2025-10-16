-- Users
MERGE INTO users (id, username, password) KEY(username)
VALUES (1, 'librarian', 'librarian');
MERGE INTO users (id, username, password) KEY(username)
VALUES (2, 'member', 'member');

-- Roles
MERGE INTO role (id, name) KEY(name) VALUES (1, 'LIBRARIAN');
MERGE INTO role (id, name) KEY(name) VALUES (2, 'MEMBER');

-- Permissions
MERGE INTO permission (id, name) KEY(name) VALUES (1, 'MANAGE_BOOKS');
MERGE INTO permission (id, name) KEY(name) VALUES (2, 'BORROW_BOOKS');

-- Role-Permission mapping
MERGE INTO role_permission (role_id, permission_id) KEY(role_id, permission_id)
VALUES (1, 1);
MERGE INTO role_permission (role_id, permission_id) KEY(role_id, permission_id)
VALUES (2, 2);

-- User-Role mapping
MERGE INTO user_role (user_id, role_id) KEY(user_id, role_id)
VALUES (1, 1);
MERGE INTO user_role (user_id, role_id) KEY(user_id, role_id)
VALUES (2, 2);

-- Libraries
MERGE INTO library (id, name) KEY(name)
VALUES (1, 'Central Library');
MERGE INTO library (id, name) KEY(name)
VALUES (2, 'East Library');

-- Books
MERGE INTO book (id, title, author, publication_year, type)
KEY(title, author, publication_year, type)
VALUES (1, 'The Lord of the Rings', 'J.R.R. Tolkien', 1954, 'BOOK');
MERGE INTO book (id, title, author, publication_year, type)
KEY(title, author, publication_year, type)
VALUES (2, 'The Hobbit', 'J.R.R. Tolkien', 1937, 'BOOK');
MERGE INTO book (id, title, author, publication_year, type)
KEY(title, author, publication_year, type)
VALUES (3, 'National Geographic', 'National Geographic Society', 1888, 'MAGAZINE');

-- Book Copies
MERGE INTO book_copy (id, book_id, library_id, status) KEY(id)
VALUES (1, 1, 1, 'AVAILABLE');
MERGE INTO book_copy (id, book_id, library_id, status) KEY(id)
VALUES (2, 1, 1, 'AVAILABLE');
MERGE INTO book_copy (id, book_id, library_id, status) KEY(id)
VALUES (3, 1, 2, 'AVAILABLE');
MERGE INTO book_copy (id, book_id, library_id, status) KEY(id)
VALUES (4, 2, 1, 'AVAILABLE');
MERGE INTO book_copy (id, book_id, library_id, status) KEY(id)
VALUES (5, 2, 2, 'AVAILABLE');
MERGE INTO book_copy (id, book_id, library_id, status) KEY(id)
VALUES (6, 3, 1, 'AVAILABLE');