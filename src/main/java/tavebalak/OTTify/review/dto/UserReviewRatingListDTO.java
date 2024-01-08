package tavebalak.OTTify.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserReviewRatingListDTO {

    private int totalCnt;
    private int pointFiveCnt;
    private int oneCnt;
    private int oneDotFiveCnt;
    private int twoCnt;
    private int twoDotFiveCnt;
    private int threeCnt;
    private int threeDotFiveCnt;
    private int fourCnt;
    private int fourDotFiveCnt;
    private int fiveCnt;

    @Builder
    public UserReviewRatingListDTO(int totalCnt, int pointFiveCnt, int oneCnt, int oneDotFiveCnt, int twoCnt, int twoDotFiveCnt, int threeCnt, int threeDotFiveCnt, int fourCnt, int fourDotFiveCnt, int fiveCnt) {
        this.totalCnt = totalCnt;
        this.pointFiveCnt = pointFiveCnt;
        this.oneCnt = oneCnt;
        this.oneDotFiveCnt = oneDotFiveCnt;
        this.twoCnt = twoCnt;
        this.twoDotFiveCnt = twoDotFiveCnt;
        this.threeCnt = threeCnt;
        this.threeDotFiveCnt = threeDotFiveCnt;
        this.fourCnt = fourCnt;
        this.fourDotFiveCnt = fourDotFiveCnt;
        this.fiveCnt = fiveCnt;
    }
}
