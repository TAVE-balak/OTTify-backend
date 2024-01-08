package tavebalak.OTTify.community.service;

import org.springframework.data.domain.Pageable;
import tavebalak.OTTify.community.dto.*;
import tavebalak.OTTify.community.entity.Community;
import tavebalak.OTTify.error.exception.NotFoundException;

import java.util.List;

public interface CommunityService {
    Community saveSubject(CommunitySubjectCreateDTO c);
    void modifySubject(CommunitySubjectEditDTO c) throws NotFoundException;
    void deleteSubject(Long subjectId) throws NotFoundException;
    CommunitySubjectsDTO findAllSubjects(Pageable pageable);
    CommunityAriclesDTO getArticleOfASubject(Long subjectId) throws NotFoundException;
    CommunitySubjectDTO getSubject(Long subjectId);
    CommunitySubjectsDTO findSingleProgramSubjects(Pageable pageable, Long programId);
    boolean likeSubject(Long subjectId);
    boolean likeComment(Long subjectId, Long commentId);
}
