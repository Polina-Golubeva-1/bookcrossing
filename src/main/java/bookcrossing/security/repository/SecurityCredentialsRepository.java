package bookcrossing.security.repository;

import bookcrossing.security.domain.SecurityCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecurityCredentialsRepository extends JpaRepository<SecurityCredentials, Long> {
    Optional<SecurityCredentials> getByUserName(String login);

    @Query(
            nativeQuery = true,
            value = "SELECT person_id FROM security WHERE user_name = ?1")
    Long findUserIdByLogin(String login);
}
