package tavebalak.OTTify.review.dto.reviewresponse;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class ReviewResponseDtoList {
    List<ReviewProgramResponseDto> reviewList = new ArrayList<>();

    public ReviewResponseDtoList(List<ReviewProgramResponseDto> reviewList){
        this.reviewList= reviewList;
    }
}
