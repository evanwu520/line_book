-- Users
INSERT INTO users (username, password) VALUES ('librarian', 'librarian');
INSERT INTO users (username, password) VALUES ('member', 'member');

-- Roles
INSERT INTO role (name) VALUES ('LIBRARIAN');
INSERT INTO role (name) VALUES ('MEMBER');

-- Permissions
INSERT INTO permission (name) VALUES ('MANAGE_BOOKS');
INSERT INTO permission (name) VALUES ('BORROW_BOOKS');

-- Role-Permission mapping
INSERT INTO role_permission (role_id, permission_id) VALUES (1, 1); -- LIBRARIAN can MANAGE_BOOKS
INSERT INTO role_permission (role_id, permission_id) VALUES (2, 2); -- MEMBER can BORROW_BOOKS

-- User-Role mapping
INSERT INTO user_role (user_id, role_id) VALUES (1, 1); -- librarian has LIBRARIAN role
INSERT INTO user_role (user_id, role_id) VALUES (2, 2); -- member has MEMBER role


-- Libraries
INSERT INTO library (name) VALUES ('Central Library');
INSERT INTO library (name) VALUES ('East Library');

-- Books
INSERT INTO book (title, author, publication_year, type) VALUES ('The Lord of the Rings', 'J.R.R. Tolkien', 1954, 'BOOK');
INSERT INTO book (title, author, publication_year, type) VALUES ('The Hobbit', 'J.R.R. Tolkien', 1937, 'BOOK');
INSERT INTO book (title, author, publication_year, type) VALUES ('National Geographic', 'National Geographic Society', 1888, 'MAGAZINE');

-- Book Copies
INSERT INTO book_copy (book_id, library_id, status) VALUES (1, 1, 'AVAILABLE');
INSERT INTO book_copy (book_id, library_id, status) VALUES (1, 1, 'AVAILABLE');
INSERT INTO book_copy (book_id, library_id, status) VALUES (1, 2, 'AVAILABLE');
INSERT INTO book_copy (book_id, library_id, status) VALUES (2, 1, 'AVAILABLE');
INSERT INTO book_copy (book_id, library_id, status) VALUES (2, 2, 'AVAILABLE');
INSERT INTO book_copy (book_id, library_id, status) VALUES (3, 1, 'AVAILABLE');