package tavebalak.OTTify.genre.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tavebalak.OTTify.genre.entity.UserGenre;
import tavebalak.OTTify.user.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGenreRepository extends JpaRepository<UserGenre, Long> {
    Optional<UserGenre> findByUserIdAndIsFirst(Long userId, boolean isFirst);
    List<UserGenre> findByGenreId(Long genreId);

    @Query("select ug from UserGenre ug where ug.user.id =:userId and ug.isFirst = true")
    UserGenre find1stGenreByUserId(@Param("userId") Long userId);

    @Query("select ug from UserGenre ug where ug.user.id =:userId and ug.isFirst = false")
    List<UserGenre> find2ndGenreByUserId(@Param("userId") Long userId);

    @Query("select ug from UserGenre ug join fetch ug.genre where ug.user.id =:userId and ug.isFirst = true")
    Optional<UserGenre> find1stGenreByUserIdFetchJoin(@Param("userId") Long userId);

    @Query("select ug from UserGenre ug join fetch ug.genre where ug.user.id =:userId and ug.isFirst = false")
    List<UserGenre> find2ndGenreByUserIdFetchJoin(@Param("userId") Long userId);

    Optional<UserGenre> findByUserAndIsFirst(User user, boolean isFirst);

    Optional<UserGenre> findByGenreIdAndUserIdAndIsFirst(@Param("genreId") Long genreId, @Param("userId") Long userId, boolean isFirst);
}
