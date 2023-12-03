package bookcrossing.repository;

import bookcrossing.domain.Book;
import bookcrossing.domain.BookRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {

    Optional<BookRequest> findById(Long id);

    BookRequest findByRequesterIdAndBookId(Long takerId, Long bookId);

    //  @Modifying
    //@Query("SELECT br FROM bookcrossing.domain.BookRequest br JOIN bookcrossing.domain.Book b WHERE br.expirationDate < :currentTimestamp AND b.status = :status")
   // List<BookRequest> findByExpirationDateBeforeAndStatus(@Param("currentTimestamp") Timestamp currentTimestamp, @Param("status") Book.BookStatus status);
}

