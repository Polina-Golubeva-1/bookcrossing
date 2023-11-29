package bookcrossing.repository;

import bookcrossing.domain.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long> {

    List<BlackList> findByRating(Integer rating);

    Optional<BlackList> findByPersonId(Long personId);
}