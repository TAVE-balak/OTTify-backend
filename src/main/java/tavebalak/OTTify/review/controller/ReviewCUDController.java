package tavebalak.OTTify.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tavebalak.OTTify.common.BaseResponse;
import tavebalak.OTTify.error.ErrorCode;
import tavebalak.OTTify.error.exception.NotFoundException;
import tavebalak.OTTify.oauth.jwt.SecurityUtil;
import tavebalak.OTTify.review.dto.reviewrequest.ReviewSaveDto;
import tavebalak.OTTify.review.dto.reviewrequest.ReviewUpdateDto;
import tavebalak.OTTify.review.service.ReviewCUDService;
import tavebalak.OTTify.user.entity.User;
import tavebalak.OTTify.user.repository.UserRepository;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewCUDController {

    private final ReviewCUDService reviewCUDService;
    private final UserRepository userRepository;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse saveReview(@RequestBody @Valid ReviewSaveDto reviewSaveDto){
        User findUser= getUser();
        reviewCUDService.saveReview(findUser,reviewSaveDto);
        return BaseResponse.success("리뷰 저장 완료");
    }

    @PutMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateReview(@PathVariable("reviewId") Long reviewId, @RequestBody @Valid ReviewUpdateDto reviewUpdateDto){
        User findUser = getUser();
        reviewCUDService.updateReview(findUser,reviewId, reviewUpdateDto);
        return BaseResponse.success("리뷰 수정 완료");
    }

    @DeleteMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deleteReview(@PathVariable("reviewId") Long reviewId){
        User findUser = getUser();
        reviewCUDService.deleteReview(findUser,reviewId);
        return BaseResponse.success("리뷰 삭제 완료");
    }

    @PostMapping("/like/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse likeReview(@PathVariable("reviewId") Long reviewId){
        User findUser = getUser();
        reviewCUDService.likeReview(findUser,reviewId);
        return BaseResponse.success("리뷰 좋아요 및 해제 완료");
    }

    private User getUser(){
        return userRepository.findByEmail(SecurityUtil.getCurrentEmail().get()).orElseThrow(()->new NotFoundException(ErrorCode.ENTITY_NOT_FOUND));
    }
}
