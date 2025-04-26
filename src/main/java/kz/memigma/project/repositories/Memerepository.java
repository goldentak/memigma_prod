package kz.memigma.project.repositories;

import kz.memigma.project.models.Meme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Memerepository extends JpaRepository<Meme, Long> {

}
