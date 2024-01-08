package tavebalak.OTTify.genre.dto;

import lombok.Builder;
import lombok.Getter;
import tavebalak.OTTify.genre.entity.UserGenre;

@Getter
public class GenreDTO {
    private Long id;
    private String name;

    public GenreDTO(UserGenre ug) {
        this.id = ug.getGenre().getId();
        this.name = ug.getGenre().getName();
    }
}
