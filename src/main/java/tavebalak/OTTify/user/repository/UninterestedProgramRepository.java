package tavebalak.OTTify.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tavebalak.OTTify.program.entity.Program;
import tavebalak.OTTify.user.entity.UninterestedProgram;
import tavebalak.OTTify.user.entity.User;

import java.util.Optional;

public interface UninterestedProgramRepository extends JpaRepository<UninterestedProgram,Long> {
    Optional<UninterestedProgram> findByProgramAndUser(Program program, User user);

    boolean existsByProgramAndUser(Program program,User user);
}
