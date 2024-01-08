package tavebalak.OTTify.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tavebalak.OTTify.user.entity.LikedReview;

import java.util.List;
import java.util.Optional;

public interface LikedReviewRepository extends JpaRepository<LikedReview, Long> {
    Optional<LikedReview> findByUserIdAndReviewId(Long userId, Long reviewId);
    List<LikedReview> findByReviewId(Long reviewId);

    boolean existsByUserIdAndReviewId(Long userId,Long reviewId);

}
