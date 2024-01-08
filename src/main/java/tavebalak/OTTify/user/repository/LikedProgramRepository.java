package tavebalak.OTTify.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tavebalak.OTTify.program.entity.Program;
import tavebalak.OTTify.user.entity.LikedProgram;
import tavebalak.OTTify.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface LikedProgramRepository extends JpaRepository<LikedProgram, Long> {
    public List<LikedProgram> findByUserId(Long userId);

    Optional<LikedProgram> findByProgramAndUser(Program program, User user);

    boolean existsByProgramAndUser(Program program,User user);
}
