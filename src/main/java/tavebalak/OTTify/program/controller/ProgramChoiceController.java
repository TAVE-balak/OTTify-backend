package tavebalak.OTTify.program.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tavebalak.OTTify.common.BaseResponse;
import tavebalak.OTTify.error.ErrorCode;
import tavebalak.OTTify.error.exception.NotFoundException;
import tavebalak.OTTify.oauth.jwt.SecurityUtil;
import tavebalak.OTTify.program.service.ProgramChoiceService;
import tavebalak.OTTify.program.service.ProgramChoiceServiceImpl;
import tavebalak.OTTify.user.entity.User;
import tavebalak.OTTify.user.repository.UserRepository;

@RestController
@RequiredArgsConstructor
public class ProgramChoiceController {

    private final UserRepository userRepository;
    private final ProgramChoiceService programChoiceService;




    @PostMapping("/api/v1/program/like/{programId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse likeProgram(@PathVariable("programId") Long programId){
        User findUser = getUser();
        programChoiceService.LikeProgram(findUser,programId);
        return BaseResponse.success("프로그램 좋아요 및 해제 실행 완료 ");
    }

    @PostMapping("/api/v1/program/uninterested/{programId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse unInterestedProgram(@PathVariable("programId") Long programId){
        User findUser = getUser();
        programChoiceService.UnInterestedProgram(findUser,programId);
        return BaseResponse.success("프로그램 관심없어요 및 해제 실행 완료");
    }


    private User getUser(){
        return userRepository.findByEmail(SecurityUtil.getCurrentEmail().get()).orElseThrow(()->new NotFoundException(ErrorCode.ENTITY_NOT_FOUND));
    }
}
