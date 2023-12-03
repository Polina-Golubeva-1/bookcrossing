package bookcrossing.repository;

import bookcrossing.domain.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

    List<Owner> findByPersonId(Long personId);
}
