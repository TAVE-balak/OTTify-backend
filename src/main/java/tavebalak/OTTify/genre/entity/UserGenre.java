package tavebalak.OTTify.genre.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tavebalak.OTTify.user.entity.User;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGenre {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    private boolean isFirst;

    @Builder
    public UserGenre(User user, Genre genre, boolean isFirst) {
        this.user = user;
        this.genre = genre;
        this.isFirst = isFirst;
    }

    public static UserGenre create(User user, Genre genre, boolean isFirst) {
        return UserGenre.builder()
                .user(user)
                .genre(genre)
                .isFirst(isFirst)
                .build();
    }

    public void changeGenre(Genre genre) {
        this.genre = genre;
    }
}
