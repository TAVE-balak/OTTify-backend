package tavebalak.OTTify.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tavebalak.OTTify.common.BaseResponse;
import tavebalak.OTTify.review.dto.reviewtagRequest.ReviewSaveTagDto;
import tavebalak.OTTify.review.dto.reviewtagResponse.ReviewTagListResponseDto;
import tavebalak.OTTify.review.service.ReviewTagService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ReviewTagController {
    private final ReviewTagService reviewTagService;

    @PostMapping("/api/v1/reviewTag")
    public BaseResponse saveReviewTag(@RequestBody @Valid ReviewSaveTagDto reviewSaveTagDto){
        reviewTagService.saveReviewTag(reviewSaveTagDto);
        return BaseResponse.success("리뷰 태그 저장 완료 ");
    }

    @GetMapping("/api/v1/reviewTag/list")
    public BaseResponse<ReviewTagListResponseDto> showReviewTags(){
        return BaseResponse.success(reviewTagService.showReviewTagList());
    }
}