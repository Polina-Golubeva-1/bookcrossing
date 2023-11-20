package bookcrossing.repository;

import bookcrossing.domain.Book;
import bookcrossing.domain.BookBorrowal;
import bookcrossing.domain.BookRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRequestRepository extends JpaRepository<BookRequest, Long>{
    BookRequest save(BookRequest request);

    Optional<BookRequest> findById(Long id);

    List<BookRequest> findAll();

    List<BookRequest> findByExpirationDateBeforeAndStatusIsNull(Timestamp expirationDate);
}