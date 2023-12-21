package tavebalak.OTTify.community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import tavebalak.OTTify.common.BaseResponse;
import tavebalak.OTTify.community.dto.*;
import tavebalak.OTTify.community.service.CommunityService;
import tavebalak.OTTify.community.service.ReplyService;
import tavebalak.OTTify.exception.NotFoundException;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/discussion")

public class CommunityController {
    private final CommunityService communityService;
    private final ReplyService replyService;

    @PostMapping("/subject")

    public BaseResponse registerSubject(@RequestBody CommunitySubjectCreateDTO c){
        communityService.saveSubject(c);
        return BaseResponse.success("성공적으로 토론주제를 생성하였습니다.");
    }

    @PutMapping("/subject")
    public BaseResponse modifySubject(@RequestBody CommunitySubjectEditDTO c) throws NotFoundException {
        communityService.modifySubject(c);
        return BaseResponse.success("성공적으로 토론주제를 수정하였습니다.");
    }

    @DeleteMapping("/subject/{subjectId}")
    public BaseResponse deleteSubject(@PathVariable Long subjectId) throws NotFoundException {
        communityService.deleteSubject(subjectId);
        return BaseResponse.success("성공적으로 토론주제를 삭제하였습니다.");
    }

    @GetMapping("/total")
    public BaseResponse getTotalProgramsSubjects(@PageableDefault(size =  10) Pageable pageable){
        CommunitySubjectsDTO page = communityService.findAllSubjects(pageable);
        return BaseResponse.success(page);
    }

    @GetMapping("/program")
    public BaseResponse getTotalProgramSubjects(@PageableDefault(size = 10) Pageable pageable,
                                               @PathParam("programId") Long programId){
        CommunitySubjectsDTO page = communityService.findSingleProgramSubjects(pageable, programId);
        return BaseResponse.success(page);
    }

    @PostMapping("/comment")
    public BaseResponse registerComment(@Valid @RequestBody final ReplyCommentCreateDTO c) throws NotFoundException {
        replyService.saveComment(c);
        return BaseResponse.success("성공적으로 토론댓글을 생성하였습니다.");
    }

    @PostMapping("/recomment")
    public BaseResponse registerRecomment(@Valid @RequestBody final ReplyRecommentCreateDTO c){
        replyService.saveRecomment(c);
        return BaseResponse.success("성공적으로 토론대댓글을 생성하였습니다.");
    }

    @PutMapping("/comment")
    public BaseResponse modifyComment(@Valid @RequestBody final ReplyCommentEditDTO c) throws NotFoundException {
        replyService.modifyComment(c);
        return BaseResponse.success("성공적으로 토론댓글을 수정하였습니다.");
    }

    @PutMapping("/recomment")
    public BaseResponse modifyRecomment(@Valid @RequestBody ReplyRecommentEditDTO c) throws NotFoundException {
        replyService.modifyRecomment(c);
        return BaseResponse.success("성공적으로 토론대댓글을 수정하였습니다.");
    }

    @DeleteMapping("/comment/{subjectId}/{commentId}")
    public BaseResponse deleteComment(@PathVariable Long subjectId, @PathVariable Long commentId) throws NotFoundException {
        replyService.deleteComment(subjectId, commentId);
        return BaseResponse.success("성공적으로 토론댓글을 삭제하였습니다.");
    }

    @DeleteMapping("/comment/{subjectId}/{commentId}/{recommentId}")
    public BaseResponse deleteRecomment(@PathVariable Long subjectId, @PathVariable Long commentId, @PathVariable Long recommentId) throws NotFoundException {
        replyService.deleteRecomment(subjectId, commentId, recommentId);
        return BaseResponse.success("성공적으로 토론대댓글을 삭제하였습니다.");
    }

    @GetMapping("/{subjectId}")
    public BaseResponse getArticles(@PathVariable Long subjectId) throws NotFoundException {
        CommunityAriclesDTO communityAriclesDTOList = communityService.getArticles(subjectId);
        return BaseResponse.success(communityAriclesDTOList);
    }
}
