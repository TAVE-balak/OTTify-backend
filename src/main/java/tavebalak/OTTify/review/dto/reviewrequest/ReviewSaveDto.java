package tavebalak.OTTify.review.dto.reviewrequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import tavebalak.OTTify.error.reviewCheck.ReviewRatingCheck;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSaveDto {

    @NotBlank(message = "contents 내용을 입력해 주세요")
    @Length(min = 8, max = 255, message = "최소 8자 이상 255자 이하로 작성해 주세요. ")
    private String contents;


    @PositiveOrZero(message = "프로그램 ID 값을 넣어 주세요")
    private Long programId;

    @ReviewRatingCheck
    private double rating;

    private List<ReviewTagIdDto> reviewTagIdDtoList;
}
