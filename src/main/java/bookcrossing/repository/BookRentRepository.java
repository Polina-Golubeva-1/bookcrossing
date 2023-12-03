package bookcrossing.repository;

import bookcrossing.domain.BookRent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRentRepository extends JpaRepository<BookRent, Long> {

    Optional<BookRent> findById(Long id);

    BookRent findByRequesterIdAndBookId(Long takerId, Long bookId);

    //  @Modifying
    //@Query("SELECT br FROM bookcrossing.domain.BookRequest br JOIN bookcrossing.domain.Book b WHERE br.expirationDate < :currentTimestamp AND b.status = :status")
   // List<BookRequest> findByExpirationDateBeforeAndStatus(@Param("currentTimestamp") Timestamp currentTimestamp, @Param("status") Book.BookStatus status);
}

