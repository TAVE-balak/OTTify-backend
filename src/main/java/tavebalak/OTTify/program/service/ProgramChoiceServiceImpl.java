package tavebalak.OTTify.program.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tavebalak.OTTify.error.ErrorCode;
import tavebalak.OTTify.error.exception.NotFoundException;
import tavebalak.OTTify.program.entity.Program;
import tavebalak.OTTify.program.repository.ProgramRepository;
import tavebalak.OTTify.user.entity.User;
import tavebalak.OTTify.user.repository.LikedProgramRepository;
import tavebalak.OTTify.user.repository.UninterestedProgramRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProgramChoiceServiceImpl implements ProgramChoiceService{
    private final ProgramRepository programRepository;
    private final LikedProgramRepository likedProgramRepository;
    private final UninterestedProgramRepository uninterestedProgramRepository;
    @Transactional
    public void LikeProgram(User user,Long programId){
        Program program = programRepository.findById(programId).orElseThrow(()->new NotFoundException(ErrorCode.PROGRAM_NOT_FOUND));

        likedProgramRepository.findByProgramAndUser(program,user).ifPresentOrElse(likedProgram ->
                user.getLikedPrograms().remove(likedProgram),()->user.likeProgram(program));

    }

    @Transactional
    public void UnInterestedProgram(User user,Long programId){
        Program program = programRepository.findById(programId).orElseThrow(()->new NotFoundException(ErrorCode.PROGRAM_NOT_FOUND));

        uninterestedProgramRepository.findByProgramAndUser(program,user).ifPresentOrElse(uninterestedProgram ->
                user.getUninterestedPrograms().remove(uninterestedProgram),()->user.unInterestedProgram(program));

    }
}
