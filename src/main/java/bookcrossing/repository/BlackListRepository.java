package bookcrossing.repository;

import bookcrossing.domain.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long> {

    List<BlackList> findByRating(Integer rating);

    List<BlackList> findByPersonId(Long personId);
}