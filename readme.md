## Environment
- **IDE**: IntelliJ
- **Maven version**: 3.9.9
- **Java version**: 8

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