package bookcrossing.repository;

import bookcrossing.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

        List<Book> findAllByName(String name);

        @Modifying
        @Query("update person  p set p.rating = :rating where p.id = :personId")
        void updatePersonRating(@Param("rating") int rating, @Param("personId") Long personId);

        List<Book> findByStatus(Book.BookStatus bookStatus);
}

