package bookcrossing.repository;

import bookcrossing.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {


        @Modifying
        @Query("update book b set b.name = :name where b.id = :id")
        void updateNameById(String name, Long id);

        List<Book> findAllByName(String name);
}

