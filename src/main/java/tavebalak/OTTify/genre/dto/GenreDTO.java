package tavebalak.OTTify.genre.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import tavebalak.OTTify.genre.entity.UserGenre;

@Getter
public class GenreDTO {

    @ApiModelProperty(value = "장르 id")
    private Long id;

    @ApiModelProperty(value = "장르 이름")
    private String name;

    public GenreDTO(UserGenre ug) {
        this.id = ug.getGenre().getId();
        this.name = ug.getGenre().getName();
    }

    public GenreDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
