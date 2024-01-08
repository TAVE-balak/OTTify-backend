package tavebalak.OTTify.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tavebalak.OTTify.community.dto.*;
import tavebalak.OTTify.community.entity.Community;
import tavebalak.OTTify.community.entity.Reply;
import tavebalak.OTTify.community.repository.CommunityRepository;
import tavebalak.OTTify.community.repository.ReplyRepository;
import tavebalak.OTTify.error.ErrorCode;
import tavebalak.OTTify.error.exception.BadRequestException;
import tavebalak.OTTify.error.exception.NotFoundException;
import tavebalak.OTTify.error.exception.UnauthorizedException;
import tavebalak.OTTify.oauth.jwt.SecurityUtil;
import tavebalak.OTTify.user.entity.User;
import tavebalak.OTTify.user.repository.UserRepository;
import tavebalak.OTTify.error.exception.NoSuchElementException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Validated
public class ReplyServiceImpl implements ReplyService{


    private final CommunityRepository communityRepository;
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    @Override
    public Reply saveComment(ReplyCommentCreateDTO c) throws NotFoundException {
        Community community = communityRepository.findById(c.getSubjectId()).orElseThrow(
                () -> new NotFoundException(ErrorCode.ENTITY_NOT_FOUND)
        );

        return replyRepository.save(Reply.builder()
                .community(community)
                .content(c.getComment())
                .user(getUser())
                .build());
    }
    @Override
    public void saveRecomment(ReplyRecommentCreateDTO c) {

        boolean hasParent = replyRepository.existsByIdAndParentId(c.getCommentId(), null);
        if(!hasParent){
            throw new NoSuchElementException(ErrorCode.ENTITY_NOT_FOUND);
        }

        Reply reply = replyRepository.save(Reply.builder()
                .community(communityRepository.findById(c.getSubjectId()).orElseThrow( () -> new BadRequestException(ErrorCode.ENTITY_NOT_FOUND)))
                .content(c.getContent())
                .user(getUser())
                .build());

        Reply parentReply = replyRepository.findById(c.getCommentId()).get();
        parentReply.addReply(reply);
    }

    @Override
    public void modifyComment(ReplyCommentEditDTO c) throws NotFoundException {
        boolean present = communityRepository.findById(c.getSubjectId()).isPresent();
        if(!present){
            throw new NoSuchElementException(ErrorCode.BAD_REQUEST);
        }

        Reply savedReply = replyRepository.findById(c.getCommentId()).orElseThrow(() -> new BadRequestException(ErrorCode.ENTITY_NOT_FOUND));

        if(!Objects.equals(savedReply.getUser().getId(), getUser().getId())){
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        ReplyCommentEditorDTO.ReplyCommentEditorDTOBuilder replyCommentEditorDTOBuilder = savedReply.toEditor();
        ReplyCommentEditorDTO edit = replyCommentEditorDTOBuilder.comment(c.getComment()).build();

        savedReply.edit(edit);

    }

    @Override
    public void modifyRecomment(ReplyRecommentEditDTO c) throws NotFoundException {

        if(!communityRepository.findById(c.getSubjectId()).isPresent()){
            throw new NoSuchElementException(ErrorCode.BAD_REQUEST);
        }
        if(!replyRepository.findById(c.getCommentId()).isPresent()){
            throw new NoSuchElementException(ErrorCode.BAD_REQUEST);
        }

        Reply savedReply = replyRepository.findById(c.getRecommentId()).orElseThrow(
                () -> new NotFoundException(ErrorCode.ENTITY_NOT_FOUND)
        );

        if(!Objects.equals(savedReply.getUser().getId(), getUser().getId())){
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        ReplyCommentEditorDTO.ReplyCommentEditorDTOBuilder reReplyCommentEditorDTOBuilder = savedReply.toEditor();
        ReplyCommentEditorDTO build = reReplyCommentEditorDTOBuilder.comment(c.getContent()).build();

        savedReply.edit(build);
    }

    @Override
    public void deleteComment(Long subjectId, Long commentId) throws NotFoundException {
        communityRepository.findById(subjectId).orElseThrow(
                () -> new NotFoundException(ErrorCode.ENTITY_NOT_FOUND)
        );

        Reply savedReply = replyRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException(ErrorCode.ENTITY_NOT_FOUND)
        );

        if(!Objects.equals(savedReply.getUser().getId(), getUser().getId())){
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        replyRepository.delete(savedReply);

    }

    @Override
    public void deleteRecomment(Long subjectId, Long commentId, Long recommentId) throws NotFoundException {
        communityRepository.findById(subjectId).orElseThrow(
                () -> new NotFoundException(ErrorCode.ENTITY_NOT_FOUND)
        );
        replyRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException(ErrorCode.ENTITY_NOT_FOUND)
        );
        Reply savedReply = replyRepository.findById(recommentId).orElseThrow(
                () -> new NotFoundException(ErrorCode.ENTITY_NOT_FOUND)
        );

        if(!Objects.equals(savedReply.getUser().getId(), getUser().getId())){
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        replyRepository.delete(savedReply);
    }

    @Override
    public List<CommentDTO> getComment(Long commentId){
        List<Reply> listComment = replyRepository.findByIdAndParentId(commentId, null);
        return listComment.stream().map(comment->
                CommentDTO.builder().commentId(comment.getId())
                .content(comment.getContent())
                .subjectId(comment.getCommunity().getId())
                .build()).collect(Collectors.toList());
    }

    public User getUser(){
        return userRepository.findByEmail(
                SecurityUtil.getCurrentEmail().get()).orElseThrow(()-> new UnauthorizedException(ErrorCode.UNAUTHORIZED)
        );
    }

}
