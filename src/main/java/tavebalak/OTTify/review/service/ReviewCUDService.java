package tavebalak.OTTify.review.service;

import tavebalak.OTTify.review.dto.reviewrequest.ReviewSaveDto;
import tavebalak.OTTify.review.dto.reviewrequest.ReviewUpdateDto;
import tavebalak.OTTify.user.entity.User;

public interface ReviewCUDService {
    void saveReview(User user, ReviewSaveDto reviewSaveDto);
    void updateReview(User user, Long reviewId, ReviewUpdateDto reviewUpdateDto);

    void deleteReview(User user, Long reviewId);

    void likeReview(User user,Long reviewId);
}
