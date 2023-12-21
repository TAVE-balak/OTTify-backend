package tavebalak.OTTify.genre.dto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OpenApiGenreListResponseDto {
    private List<OpenApiGenreDto> genres=new ArrayList<>();
}
