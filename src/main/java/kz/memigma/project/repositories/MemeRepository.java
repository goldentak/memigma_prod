package kz.memigma.project.repositories;

import kz.memigma.project.models.Meme;
import kz.memigma.project.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemeRepository extends JpaRepository<Meme, Long> {
    Page<Meme> findByIdNotIn(List<Long> excludedIds, Pageable pageable);
    List<Meme> findTop5ByIdInOrderByCreationDateDesc(List<Long> ids);
}