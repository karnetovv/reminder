package cos.corp.domain.repository;

import cos.corp.domain.entity.Reminder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long>, JpaSpecificationExecutor<Reminder> {

    Optional<Reminder> findByIdAndUserId(Long id, Long userId);

    Page<Reminder> findAllByUserId(Long userId, Pageable pageable);

    @Query("select r from Reminder r join fetch r.user where r.id = :id")
    Optional<Reminder> findByIdWithUser(@Param("id") Long id);
}
