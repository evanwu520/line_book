## **Introduction**

This project is an **Online Library System** featuring two main functions.  
The first allows **users** to borrow and return books, as well as perform advanced book searches.  
The second enables **librarians** to manage the book inventory — including adding, updating, and maintaining book records.  
The system is built with **Spring Boot** and secured using **Spring Security with JWT**, providing a robust and secure **RESTful API** backend.

## **Environment**
- **IDE:** IntelliJ IDEA  
- **Build Tool:** Maven 3.9.9  
- **Java Version:** 8  
- **Spring Boot Version:** 2.3.1.RELEASE

---

## How to Run and Explore the Application

- **Swagger UI for API Testing:**  
  When the application is running, you can explore and test all API endpoints in your browser at:  
  [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
  - Swagger UI provides detailed request and response formats, parameter descriptions, and example values.
  - You can execute API calls directly from the Swagger interface.

- **H2 Database Console:**  
  When the application is running, you can view and query the in-memory H2 database at:  
  [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - **Login credentials:**  
    - **Username**: `sa`  
    - **Password**: *(leave blank)*

### Default Users

- **LIBRARIAN (MANAGE_BOOKS):**  
  - Username: `librarian`  
  - Password: `librarian`
- **MEMBER (BORROW_BOOKS):**  
  - Username: `member`  
  - Password: `member`

---

## API Endpoints Requiring Authentication

Below are the main endpoints that require a valid JWT token and appropriate user permissions:

### `/api/loans` (LoanController)
- **POST `/api/loans/borrow`**  
  Requires: `BORROW_BOOKS` authority  
  ```java
  @PostMapping("/borrow")
  @PreAuthorize("hasAuthority('BORROW_BOOKS')")
  ```
- **POST `/api/loans/return`**  
  Requires: `BORROW_BOOKS` authority  
  ```java
  @PostMapping("/return")
  @PreAuthorize("hasAuthority('BORROW_BOOKS')")
  ```

### `/api/books` (BookController)
- **POST `/api/books`**  
  Requires: `MANAGE_BOOKS` authority  
  ```java
  @PostMapping
  @PreAuthorize("hasAuthority('MANAGE_BOOKS')")
  ```
- **PUT `/api/books`**  
  Requires: `MANAGE_BOOKS` authority  
  ```java
  @PutMapping
  @PreAuthorize("hasAuthority('MANAGE_BOOKS')")
  ```
- **GET `/api/books/search`**  
  Requires: `BORROW_BOOKS` or `MANAGE_BOOKS` authority  
  ```java
  @GetMapping("/search")
  @PreAuthorize("hasAnyAuthority('BORROW_BOOKS','MANAGE_BOOKS')")
  ```

---

## Notes

- **Authentication Filter:**  
  The `JwtAuthFilter` checks for a valid JWT token in the `Authorization` header before allowing access to secured endpoints.
- **Authorization:**  
  Access is granted based on the user's permissions extracted from the JWT token.
- **Public Endpoints:**  
  Endpoints under `/api/auth` (login, register) do **not** require authentication.

---

## Example Request

```http
POST /api/loans/borrow
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "bookId": 1,
  "libraryId": 2
}
```

If the token is missing or the user lacks the required authority, the API will return a `401 Unauthorized` or `403 Forbidden` response.

---