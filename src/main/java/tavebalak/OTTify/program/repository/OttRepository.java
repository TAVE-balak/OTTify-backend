package tavebalak.OTTify.program.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tavebalak.OTTify.program.entity.Ott;

public interface OttRepository extends JpaRepository<Ott, Long> {
}
