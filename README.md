# BookCrossing
This application allows users to exchange books.
After registration, the user can reserve and rent books, as well as provide their books for rent.

### Technologies
- Java
- Spring Framework
- Spring Security
- Spring Data JPA
- Spring Boot Actuator
- JUnit
- Spring Test
- Lombok
- PostgreSQL- JWT (jjwt)
- Jacoco Maven Plugin
- Jakarta Persistence API
### Requirements
- Java Development Kit (JDK) 17.
- PostgreSQL server with "my-project" database.

### Launch of the project
1. Clone the repository: `git clone https://github.com/Polina-Golubeva-1/bookcrossing`
2. Go to the project directory: `cd bookcrossing`
3. Launch of the project: `mvnw spring-boot:run`

### Database
The project is connected to an in-memory PostgreSQL database. 
It contains 6 tables. Person - stores information about users.
Book - stores data about books. BookRent - stores data about book reservations. 
BookBorrowal - stores data about book rentals. BlackList - stores data about blocked users. 
Security - stores data about login, password (in encrypted form), and user data for identification purposes.

### Authentication
Authentication is required to access the database operations. 
Upon authentication, the user has the role USER or ADMIN. 
The security database table contains (Login: login, Password: password).
After providing the data, we receive a token and use it for further operations.   
