package cos.corp.domain.repository;

import cos.corp.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByExternalId(String externalId);

    Optional<User> findByEmail(String email);

    void deleteByExternalId(String externalId);
}
