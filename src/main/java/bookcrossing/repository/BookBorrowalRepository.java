package bookcrossing.repository;

import bookcrossing.domain.BookBorrowal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookBorrowalRepository extends JpaRepository<BookBorrowal, Long> {
    BookBorrowal save(BookBorrowal borrowal);

    Optional<BookBorrowal> findById(Long id);

    List<BookBorrowal> findAll();
}
