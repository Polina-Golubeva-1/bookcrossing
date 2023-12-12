package bookcrossing.repository;

import bookcrossing.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Modifying
    @Query("update person  p set p.rating = :rating where p.id = :personId")
    void updatePersonRating(@Param("rating") int rating, @Param("personId") Long personId);

}