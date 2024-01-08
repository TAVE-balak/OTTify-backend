package tavebalak.OTTify.review.dto.reviewtagResponse;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class ReviewTagListResponseDto {
    private List<ReviewTagInfo> reviewTagInfoList = new ArrayList<>();

    public ReviewTagListResponseDto(List<ReviewTagInfo> reviewTagInfoList){
        this.reviewTagInfoList=reviewTagInfoList;
    }
}
