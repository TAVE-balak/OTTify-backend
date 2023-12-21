package tavebalak.OTTify.genre.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Genre {
    @Id @GeneratedValue
    @Column(name = "genre_id")
    private Long id;
    private String name;
    private Long tmDbGenreId;

    @Builder
    Genre(String name,Long tmDbGenreId){
        this.name=name;
        this.tmDbGenreId=tmDbGenreId;
    }
}
