package tavebalak.OTTify.review.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tavebalak.OTTify.program.entity.Program;
import tavebalak.OTTify.review.entity.Review;
import tavebalak.OTTify.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByProgramAndUser(Program program, User user);

    Optional<Review> findByProgramAndUser(Program program, User user);

    //좋아요 많이 받은 순으로 3개 범위: 전체

    List<Review> findTop3ByProgramOrderByLikeNumberDesc(Program program);

    List<Review> findTop4ByProgramOrderByLikeNumberDesc(Program program);


    // Slice 로 전체 리뷰 가져오기
    Slice<Review> findByProgram(Program program, Pageable pageable);


    //좋아요 많이 받은 순으로 user first genre 3개, 나랑 취향이 비슷한 user의 장르
    @Query("select r from Review r where r.program=:program and r.genre=:genre order by r.likeNumber desc")
    List<Review> findUserSpecific4ByProgramAndGenreOrderByLikeNumberDesc(@Param("program") Program program,@Param("genre") String genre,Pageable pageable);

    //좋아요 많이 받은 순으로 유저 first genre 같은 애들, 범위 전체

    @Query("select r from Review r where r.program=:program and r.genre=:genre")
    Slice<Review> findUserSpecificByProgramAndGenre(@Param("program") Program program,@Param("genre") String genre,Pageable pageable);



    @Query("select count(r) from Review r where r.genre=:genre and r.program=:program")
    long countByMyGenreName(@Param("genre") String genre,@Param("program") Program program);



    @Query("select sum(r.rating) from Review r where r.genre=:genre and r.program=:program")
    Double sumReviewRatingByGenreName(@Param("genre")String genreName,@Param("program")Program program);


}
