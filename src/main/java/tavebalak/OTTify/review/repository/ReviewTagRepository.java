package tavebalak.OTTify.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tavebalak.OTTify.review.entity.ReviewTag;

public interface ReviewTagRepository extends JpaRepository<ReviewTag,Long> {
}
