package tavebalak.OTTify.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import tavebalak.OTTify.user.entity.LikedProgram;

import java.util.List;

public interface LikedProgramRepository extends JpaRepository<LikedProgram, Long> {
    List<LikedProgram> findByUserId(@Param("userId") Long userId);
}
