package tavebalak.OTTify.user.dto.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tavebalak.OTTify.common.constant.GradeType;
import tavebalak.OTTify.genre.dto.GenreDTO;
import tavebalak.OTTify.genre.dto.OpenApiGenreDto;
import tavebalak.OTTify.genre.entity.Genre;
import tavebalak.OTTify.review.dto.UserReviewRatingListDTO;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserProfileDTO {
    private String profilePhoto;
    private String nickName;
    private GradeType grade;
    private String email;

    private double averageRating;
    private UserReviewRatingListDTO ratingList;

    private GenreDTO firstGenre;
    private List<GenreDTO> secondGenre = new ArrayList<GenreDTO>();

    private UserOttListDTO ott;

    private LikedProgramListDTO likedProgram;
    private UninterestedProgramListDTO uninterestedProgram;

    @Builder
    public UserProfileDTO(String profilePhoto, String nickName, GradeType grade, String email, double averageRating, UserReviewRatingListDTO ratingList, GenreDTO firstGenre, List<GenreDTO> secondGenre, UserOttListDTO ott, LikedProgramListDTO likedProgram, UninterestedProgramListDTO uninterestedProgram) {
        this.profilePhoto = profilePhoto;
        this.nickName = nickName;
        this.grade = grade;
        this.email = email;
        this.averageRating = averageRating;
        this.ratingList = ratingList;
        this.firstGenre = firstGenre;
        this.secondGenre = secondGenre;
        this.ott = ott;
        this.likedProgram = likedProgram;
        this.uninterestedProgram = uninterestedProgram;
    }
}
