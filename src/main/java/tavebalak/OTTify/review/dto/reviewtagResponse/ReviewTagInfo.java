package tavebalak.OTTify.review.dto.reviewtagResponse;


import lombok.Getter;
import tavebalak.OTTify.review.entity.ReviewTag;

@Getter
public class ReviewTagInfo {
    private Long id;
    private String name;

    public ReviewTagInfo(ReviewTag reviewTag){
        this.id=reviewTag.getId();
        this.name=reviewTag.getName();
    }
}