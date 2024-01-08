package tavebalak.OTTify.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tavebalak.OTTify.common.BaseResponse;
import tavebalak.OTTify.error.ErrorCode;
import tavebalak.OTTify.error.exception.NotFoundException;
import tavebalak.OTTify.oauth.jwt.SecurityUtil;
import tavebalak.OTTify.review.dto.reviewresponse.ReviewProgramResponseDto;
import tavebalak.OTTify.review.dto.reviewresponse.ReviewResponseDtoList;
import tavebalak.OTTify.review.service.ReviewShowProgramDetailService;
import tavebalak.OTTify.user.entity.User;
import tavebalak.OTTify.user.repository.UserRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/{programId}")
public class ReviewShowProgramDetailController {
    private final ReviewShowProgramDetailService reviewShowProgramDetailService;
    private final UserRepository userRepository;

    @GetMapping("/normal/reviews/count/4")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<ReviewResponseDtoList> show4ReviewList(@PathVariable("programId") Long programId){
        return BaseResponse.success(reviewShowProgramDetailService.show4Review(programId));
    }


    @GetMapping("/normal/reviews")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Slice<ReviewProgramResponseDto>> showReviewListALL(@PathVariable("programId") Long programId,
                                                                           @PageableDefault(size = 10,
                                                                                   sort = "likeNumber",
                                                                                   direction = Sort.Direction.DESC,
                                                                                   page = 0) Pageable pageable){
        return BaseResponse.success(reviewShowProgramDetailService.showReviewList(programId,pageable));
    }


    @GetMapping("/user/specific/reviews/count/4")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<ReviewResponseDtoList> show4UserSpecificReviewList(@PathVariable("programId") Long programId){
        User findUser= getUser();
        return BaseResponse.success(reviewShowProgramDetailService.show4UserSpecificReviewList(findUser,programId));
    }

    @GetMapping("/user/specific/reviews")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Slice<ReviewProgramResponseDto>> showUserSpecificReviewListALL(@PathVariable("programId") Long programId,
                                                                                       @PageableDefault(size = 10,
                                                                                               sort = "likeNumber",
                                                                                               direction = Sort.Direction.DESC,
                                                                                               page = 0) Pageable pageable){
        User findUser = getUser();
        return BaseResponse.success(reviewShowProgramDetailService.showUserSpecificReviewList(findUser,programId,pageable));
    }





    @GetMapping("/user/specific/review/rating")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<String> showUserSpecificReviewRating(@PathVariable("programId") Long programId){
        User findUser= getUser();
        return BaseResponse.success(reviewShowProgramDetailService.showUserSpecificRating(findUser,programId));
    }

    private User getUser(){
        return userRepository.findByEmail(SecurityUtil.getCurrentEmail().get()).orElseThrow(()->new NotFoundException(ErrorCode.ENTITY_NOT_FOUND));
    }
}
