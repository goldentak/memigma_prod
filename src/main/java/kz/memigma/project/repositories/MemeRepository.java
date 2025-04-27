package kz.memigma.project.repositories;

import kz.memigma.project.models.Meme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemeRepository extends JpaRepository<Meme, Long> {
    Page<Meme> findByIdNotIn(List<Long> excludedIds, Pageable pageable);
}