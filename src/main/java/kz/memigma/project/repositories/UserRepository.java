package kz.memigma.project.repositories;

import kz.memigma.project.models.Meme;
import kz.memigma.project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u.password FROM User u WHERE u.username = :username")
    String findPasswordByUsername(@Param("username") String username);

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    void deleteAllByEnabledFalseAndCreatedAtBefore(Instant time);

    List<User> findTop5ByIdInOrderByCreatedAtDesc(List<Long> ids);


}
