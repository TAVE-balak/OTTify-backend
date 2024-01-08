package tavebalak.OTTify.review.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import tavebalak.OTTify.review.dto.reviewresponse.ReviewProgramResponseDto;
import tavebalak.OTTify.review.dto.reviewresponse.ReviewResponseDtoList;
import tavebalak.OTTify.user.entity.User;

public interface ReviewShowProgramDetailService {
    ReviewResponseDtoList show4Review(Long programId);

    Slice<ReviewProgramResponseDto> showReviewList(Long programId, Pageable pageable);

    ReviewResponseDtoList show4UserSpecificReviewList(User user, Long programId);

    Slice<ReviewProgramResponseDto> showUserSpecificReviewList(User user, Long programId, Pageable pageable);

    String showUserSpecificRating(User user,Long programId);


}
