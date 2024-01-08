package tavebalak.OTTify.program.service;

import tavebalak.OTTify.user.entity.User;

public interface ProgramChoiceService {
    void LikeProgram(User user, Long programId);
    void UnInterestedProgram(User user,Long programId);


}
