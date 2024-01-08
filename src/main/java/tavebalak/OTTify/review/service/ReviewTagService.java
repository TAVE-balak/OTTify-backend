package tavebalak.OTTify.review.service;


import tavebalak.OTTify.review.dto.reviewtagRequest.ReviewSaveTagDto;
import tavebalak.OTTify.review.dto.reviewtagResponse.ReviewTagListResponseDto;

public interface ReviewTagService {
    void saveReviewTag(ReviewSaveTagDto reviewSaveTagDto);
    ReviewTagListResponseDto showReviewTagList();
    void basicReviewTagSave();
}