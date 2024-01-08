package tavebalak.OTTify.genre.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class GenreUpdateDTO {

    @NotNull
    private Long id;
}
