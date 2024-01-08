package tavebalak.OTTify.review.dto.reviewtagRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSaveTagDto {

    @NotBlank(message = "태그 이름을 입력해주세요.")
    private String name;
}