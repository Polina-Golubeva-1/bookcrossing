# BookCrossing
This application allows users to exchange books.
After registration, the user can reserve and rent books, as well as provide their books for rent.

## Technologies
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

#### Project structure
bookcrossing/
src/
------ main/
---------- java/
--------------bookcrossing/
----------------- controller/
---------------------BlackListController.java
---------------------BookBorrowalController.java
---------------------BookController.java
---------------------BookRentController.java
---------------------PersonController.java
-------------- domain/
-------------------- BlackList.java
-------------------- Book.java
-------------------- BookBorrowal.java
-------------------- BookRent.java
-------------------- Person.java
-------------------- Role.java
--------------exeption_resolver/
-------------------- BookNotFoundException.java
-------------------- BookUnavailableException.java
-------------------- SameUserInDatabaseException.java
-------------------- UserFromDatabaseNotFound.java
-------------------- Role.java
--------------service/
--------------------BlackListService.java
--------------------BookBorrowalService.java
--------------------BookService.java
--------------------BookRentService.java
--------------------PersonService.java
--------------repository/
-------------------- BlackListRepository.java
--------------------BookBorrowalRepository.java
--------------------BookRepository.java
--------------------BookRentRepository.java
--------------------PersonRepository.java
--------------Main.java
-------------- resources/
----------------- application.properties
--------------─ webapp/
--------------test/
--------resources/
--- webapp/
-- target/
-pom.xml

##### Launch of the project
1. Clone the repository: `git clone https://github.com/Polina-Golubeva-1/bookcrossing`
2. Go to the project directory: `cd bookcrossing`
3. Launch of the project: `mvnw spring-boot:run`
   
