package tavebalak.OTTify.user.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tavebalak.OTTify.common.constant.GradeType;
import tavebalak.OTTify.common.constant.Role;
import tavebalak.OTTify.common.constant.SocialType;
import tavebalak.OTTify.common.entity.BaseEntity;
import tavebalak.OTTify.genre.entity.Genre;
import tavebalak.OTTify.genre.entity.UserGenre;
import tavebalak.OTTify.program.entity.Program;
import tavebalak.OTTify.review.entity.Review;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "\"user\"")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String email;
    private String nickName;
    private String profilePhoto;
    private double averageRating;

    @Enumerated(EnumType.STRING)
    private GradeType grade;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String code; //구글, 네이버의 로그아웃, 탈퇴를 위한 코드

    private int userReviewCounts;
    
    @Builder
    public User(String email, String nickName, String profilePhoto, SocialType socialType, Role role, String code) {
        this.email = email;
        this.nickName = nickName;
        this.profilePhoto = profilePhoto;
        this.averageRating = 0;
        this.grade = GradeType.GENERAL;
        this.socialType = socialType;
        this.role = role;
        this.code = code;
        this.userReviewCounts = 0;//userReviewCounts 추가
    }


    // user 와 genre 의 연관관계 편의 메서드 및 양방향 관계
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UserGenre> userGenreList = new ArrayList<>();
    public void addGenre(Genre genre, boolean isFirst){
        UserGenre userGenre=UserGenre.builder()
                .genre(genre)
                .user(this)
                .isFirst(isFirst)
                .build();

        userGenreList.add(userGenre);
    }


    //리뷰 추가시 userReviewCount 증가 및 평균 재계산.
    public void addUsersReviewAndRecalculateRating(double ratings){
        double beforeSumRatings = this.averageRating * this.userReviewCounts;
        double afterSumRatings = beforeSumRatings + ratings;

        this.userReviewCounts++;
        this.averageRating = afterSumRatings / this.userReviewCounts;

    }

    //리뷰 변화시 평균 재계산
    public void changeUsersReviewAndRecalculateRating(double beforeRatings,double afterRatings){
        double beforeSumRatings = this.averageRating * this.userReviewCounts;
        double afterSumRatings = beforeSumRatings - beforeRatings + afterRatings;

        this.averageRating = afterSumRatings / this.userReviewCounts;
    }


    //리뷰 삭제시 userReviewCount 감소 및 평균 재계산
    public void deleteUsersReviewAndRecalculateRating(double deleteRatings){
        double beforeSumRatings = this.averageRating * this.userReviewCounts;
        double afterSumRatings  =  beforeSumRatings - deleteRatings;

        this.userReviewCounts--;
        if(this.userReviewCounts == 0 ){
            this.averageRating = 0;
        }
        else{
            this.averageRating = afterSumRatings / this.userReviewCounts;
        }
    }

    //좋아요 하는 리뷰 프로그램 연관관계 세팅 및 양방향 관계

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<LikedReview> likedReviews = new ArrayList<>();

    public void likeReview(Review review){
        LikedReview likedReview=LikedReview.builder()
                .review(review)
                .user(this)
                .build();

        this.likedReviews.add(likedReview);

    }

    //좋아요 하는 프로그램 연관관계 편의메서드 생성 및 양방향 관계 세팅

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL , orphanRemoval = true)
    private List<LikedProgram> likedPrograms = new ArrayList<>();

    public void likeProgram(Program program){
        LikedProgram likedProgram = LikedProgram.builder()
                .program(program)
                .user(this)
                .build();

        this.likedPrograms.add(likedProgram);
    }


    //싫어 하는 프로그램 연관관계 편의 메서드 및 양방향 관계 세팅
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL , orphanRemoval = true)
    private List<UninterestedProgram> uninterestedPrograms = new ArrayList<>();

    public void unInterestedProgram(Program program){
        UninterestedProgram uninterestedProgram = UninterestedProgram.builder()
                .program(program)
                .user(this)
                .build();

        this.uninterestedPrograms.add(uninterestedProgram);
    }

    public static TestUserBuilder testUserBuilder(){
        return new TestUserBuilder();
    }

    public static class TestUserBuilder extends User {
        private Long id;
        private String nickName;
        private String profilePhoto;
        private double averageRating;
        TestUserBuilder(){
        }

        public User create(final Long id, String nickName, String profilePhoto, double averageRating){
            this.id = id;
            this.nickName = nickName;
            this.profilePhoto = profilePhoto;
            this.averageRating = averageRating;
            return this;
        }
    }

        public void changeUserRole(){
        role = Role.USER;
    }

    public void changeCode(String code) { this.code = code; }

}
