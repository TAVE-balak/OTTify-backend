package tavebalak.OTTify.review.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tavebalak.OTTify.common.entity.BaseEntity;
import tavebalak.OTTify.program.entity.Program;
import tavebalak.OTTify.user.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    private Program program;

    private String content;
    private double rating;
    private String genre;

    //좋아요 수 필드 추가
    @Column(name = "like_counts")
    private int likeCounts;

    @Builder
    public Review(String content, double rating, String genre, User user, Program program) {
        this.content = content;
        this.rating = rating;
        this.genre = genre;
        this.user = user;
        this.program = program;
        this.likeCounts = 0;
    }


    //리뷰 태그 연관관계 편의 메서드 추가 및 양방향 관계 세팅
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewReviewTag> reviewReviewTags = new ArrayList<>();

    public void addReviewTag(ReviewTag reviewTag) {
        ReviewReviewTag reviewReviewTag = ReviewReviewTag.builder()
            .reviewTag(reviewTag)
            .review(this)
            .build();

        this.reviewReviewTags.add(reviewReviewTag);
    }

    //좋아요 수 증가
    public void addLikeNumber() {
        this.likeCounts++;
    }

    //좋아요 수 취소
    public void cancelLikeNumber() {
        this.likeCounts--;
    }

    public void changeContentAndRatingReview(String content, double rating) {
        this.content = content;
        this.rating = rating;
    }
}
