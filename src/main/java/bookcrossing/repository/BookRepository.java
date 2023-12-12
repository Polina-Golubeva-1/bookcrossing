package bookcrossing.repository;

import bookcrossing.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByName(String name);

    List<Book> findByStatus(Book.BookStatus bookStatus);
}

