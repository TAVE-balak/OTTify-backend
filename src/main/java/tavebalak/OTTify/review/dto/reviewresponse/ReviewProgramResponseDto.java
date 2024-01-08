package tavebalak.OTTify.review.dto.reviewresponse;

import lombok.Builder;
import lombok.Data;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class ReviewProgramResponseDto {
    private Long reviewId;
    private int like;
    private String contents;
    private String userPosterPath;
    private String userNickName;
    private String createdDate;
    private String ratings;
    private List<String> reviewTagNames = new ArrayList<>();

    @Builder
    public ReviewProgramResponseDto(Long reviewId, int like, String contents, String userPosterPath, String userNickName, LocalDateTime createdDateTime, double ratings, List<String> reviewTagNames){
        this.reviewId = reviewId;
        this.like=like;
        this.contents=contents;
        this.userPosterPath = userPosterPath;
        this.userNickName = userNickName;
        this.createdDate = createdDateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm"));
        this.ratings =String.format("%.1f", ratings);
        this.reviewTagNames = reviewTagNames;
    }
}
