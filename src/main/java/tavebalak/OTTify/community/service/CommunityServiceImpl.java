package tavebalak.OTTify.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tavebalak.OTTify.community.dto.*;
import tavebalak.OTTify.community.entity.Community;
import tavebalak.OTTify.community.entity.Reply;
import tavebalak.OTTify.community.repository.CommunityRepository;
import tavebalak.OTTify.community.repository.ReplyRepository;
import tavebalak.OTTify.error.ErrorCode;
import tavebalak.OTTify.error.exception.BadRequestException;
import tavebalak.OTTify.exception.NotFoundException;
import tavebalak.OTTify.oauth.jwt.SecurityUtil;
import tavebalak.OTTify.program.entity.Program;
import tavebalak.OTTify.program.repository.ProgramRepository;
import tavebalak.OTTify.user.entity.User;
import tavebalak.OTTify.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService{
    private final CommunityRepository communityRepository;
    private final ProgramRepository programRepository;
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    @Override
    public Community saveSubject(CommunitySubjectCreateDTO c){

        boolean present = programRepository.findById(c.getProgramId()).isPresent();
        Program program = null;
        if(!present) {
            program = programRepository.save(Program.builder().id(c.getProgramId()).title(c.getProgramTitle()).posterPath(c.getPosterPath()).build());
        }else{
            program = programRepository.findById(c.getProgramId()).get();
        }

        Community community = Community.builder()
                .title(c.getSubjectName())
                .content(c.getContent())
                .user(getUser())
                .program(program)
                .build();

        return communityRepository.save(community);

    }
    @Override
    public void modifySubject(CommunitySubjectEditDTO c) throws NotFoundException {
        Community community = communityRepository.findById(c.getSubjectId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        if(community.getUser().getId() != getUser().getId()){
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        boolean present = programRepository.findById(c.getProgramId()).isPresent();
        Program program = null;
        if(!present){
            program = programRepository.save(Program.builder().title(c.getProgramTitle()).posterPath(c.getPosterPath()).build());
        }else {
            program = programRepository.findById(c.getProgramId()).get();
        }

        CommunitySubjectEditorDTO.CommunitySubjectEditorDTOBuilder communitySubjectEditorDTOBuilder = community.toEditor();
        CommunitySubjectEditorDTO communitySubjectEditorDTO = communitySubjectEditorDTOBuilder
                                                                .title(c.getSubjectName())
                                                                .content(c.getContent())
                                                                .program(program)
                                                                .build();

        community.edit(communitySubjectEditorDTO);
    }

    @Override
    public void deleteSubject(Long subjectId) throws NotFoundException {
        Community community = communityRepository.findById(subjectId).orElseThrow(() -> new NotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        if(!Objects.equals(community.getUser().getId(), getUser().getId())){
            throw new IllegalAccessError();
        }
        communityRepository.delete(community);
    }

    @Override
    public CommunitySubjectsDTO findAllSubjects(Pageable pageable) {
        Page<Community> communities = communityRepository.findCommunitiesBy(pageable);
        List<CommunitySubjectsListDTO> listDTO = communities.stream().map(
                community -> CommunitySubjectsListDTO
                        .builder()
                        .createdAt(community.getCreatedAt())
                        .updatedAt(community.getUpdatedAt())
                        .title(community.getTitle())
                        .content(community.getContent())
                        .nickName(community.getUser().getNickName())
                        .programId(community.getProgram().getId())
                        .build()
        ).collect(Collectors.toList());
        return  CommunitySubjectsDTO.builder().subjectAmount(communities.getTotalElements()).list(listDTO).build();
    }

    @Override
    public CommunitySubjectsDTO findSingleProgramSubjects(Pageable pageable, Long programId) {
        Page<Community> communities = communityRepository.findCommunitiesByProgramId(pageable, programId);
        List<CommunitySubjectsListDTO> list = communities.stream().map(
                community -> CommunitySubjectsListDTO
                        .builder()
                        .createdAt(community.getCreatedAt())
                        .updatedAt(community.getUpdatedAt())
                        .title(community.getTitle())
                        .content(community.getContent())
                        .nickName(community.getUser().getNickName())
                        .programId(programId)
                        .build()
        ).collect(Collectors.toList());

        return CommunitySubjectsDTO.builder().subjectAmount(communities.getTotalElements()).list(list).build();
    }

    @Override
    public CommunityAriclesDTO getArticles(Long subjectId) throws NotFoundException {
        Community community = communityRepository.findById(subjectId).orElseThrow(
                () -> new NotFoundException(ErrorCode.ENTITY_NOT_FOUND)
        );

        List<Reply> replyList = replyRepository.findByCommunityIdAndParentId(community.getId(), null);
        List<CommentListsDTO> commentListsDTOList = new ArrayList<>();
        for (Reply comment : replyList) {
            List<Reply> byCommunityIdAndParentId = replyRepository.findByCommunityIdAndParentId(community.getId(), comment.getId());
            List<ReplyListsDTO> collect = byCommunityIdAndParentId.stream().map(listone ->
                    ReplyListsDTO.builder()
                            .recommentId(listone.getId())
                            .content(listone.getContent())
                            .nickName(listone.getUser().getNickName())
                            .userId(listone.getUser().getId())
                            .createdAt(listone.getCreatedAt())
                            .build()
            ).collect(Collectors.toList());

            CommentListsDTO build = CommentListsDTO.builder()
                    .content(comment.getContent())
                    .nickName(comment.getUser().getNickName())
                    .createdAt(comment.getCreatedAt())
                    .userId(comment.getUser().getId())
                    .replyListsDTOList(collect)
                    .build();
            commentListsDTOList.add(build);
        }


        return CommunityAriclesDTO.builder()
                .title(community.getTitle())
                .writer(community.getUser().getNickName())
                .content(community.getContent())
                .createdAt(community.getCreatedAt())
                .updatedAt(community.getUpdatedAt())
                .commentAmount(replyList.size())
                .commentListsDTOList(commentListsDTOList)
                .build();
    }

    @Override
    public CommunitySubjectDTO getArticle(Long subjectId) {
        Community community = communityRepository.findById(subjectId).orElseThrow(() -> new NoSuchElementException(ErrorCode.ENTITY_NOT_FOUND.getMessage()));

        return CommunitySubjectDTO.builder()
                .subjectId(subjectId)
                .title(community.getTitle())
                .content(community.getContent())
                .programId(community.getProgram().getId())
                .programTitle(community.getProgram().getTitle())
                .posterPath(community.getProgram().getPosterPath())
                .build();
    }

    public User getUser(){
        return userRepository.findByEmail(SecurityUtil.getCurrentEmail().get()).orElseThrow(()-> new SecurityException());
    }

}
