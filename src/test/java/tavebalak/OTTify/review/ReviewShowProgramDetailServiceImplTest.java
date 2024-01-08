package tavebalak.OTTify.review;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tavebalak.OTTify.genre.entity.Genre;
import tavebalak.OTTify.genre.repository.GenreRepository;
import tavebalak.OTTify.program.entity.Program;
import tavebalak.OTTify.program.entity.ProgramType;
import tavebalak.OTTify.program.repository.ProgramRepository;
import tavebalak.OTTify.review.dto.reviewrequest.ReviewSaveDto;
import tavebalak.OTTify.review.dto.reviewtagRequest.ReviewSaveTagDto;
import tavebalak.OTTify.review.dto.reviewrequest.ReviewTagIdDto;
import tavebalak.OTTify.review.entity.Review;
import tavebalak.OTTify.review.repository.ReviewRepository;
import tavebalak.OTTify.review.repository.ReviewTagRepository;
import tavebalak.OTTify.review.service.ReviewCUDService;
import tavebalak.OTTify.review.service.ReviewShowProgramDetailService;
import tavebalak.OTTify.review.service.ReviewShowProgramDetailServiceImpl;
import tavebalak.OTTify.review.service.ReviewTagServiceImpl;
import tavebalak.OTTify.user.entity.User;
import tavebalak.OTTify.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ReviewShowProgramDetailServiceImplTest {

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProgramRepository programRepository;

    @Autowired
    ReviewShowProgramDetailServiceImpl reviewShowProgramDetailServiceImpl;

    @Autowired
    ReviewTagServiceImpl reviewTagService;

    @Autowired
    ReviewTagRepository reviewTagRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ReviewCUDService reviewCUDService;

    @Autowired
    ReviewShowProgramDetailService reviewShowProgramDetailService;

    //프로그램 별 장르 별점의 변화와 전체 장르 별점의 변화를 테스트하고
    //user 의 평균 별점 변화를 테스트 합니다.

    @Test
    @Rollback(value = true)
    @Transactional
    void saveReview() {
        User user=makeUser();
        Program program=makeProgram();


        List<ReviewTagIdDto> reviewTagIdDtoList=makeReviewTag();


        ReviewSaveDto reviewSaveDto1 = new ReviewSaveDto("아 진짜 꿀잼이였어요ㅠ",program.getId(),3.8,reviewTagIdDtoList);



        reviewCUDService.saveReview(user,reviewSaveDto1);

        Review findReview = reviewRepository.findByProgramAndUser(program,user).get();

        Assertions.assertThat(findReview.getContent()).isEqualTo("아 진짜 꿀잼이였어요ㅠ");

        Assertions.assertThat(program.getReviewCount()).isEqualTo(1);
        Assertions.assertThat(program.getAverageRating()).isEqualTo(3.8);



        User user2=makeUser2();

        ReviewSaveDto reviewSaveDto2 = new ReviewSaveDto("애니메이션 장르 좋아하는데 꿀잼이였어요~",program.getId(),4.2,reviewTagIdDtoList);



        reviewCUDService.saveReview(user2,reviewSaveDto2);

        Review findReview2=reviewRepository.findByProgramAndUser(program,user2).get();

        Assertions.assertThat(program.getReviewCount()).isEqualTo(2);
        Assertions.assertThat(program.getAverageRating()).isEqualTo(4);


        User user3=makeUser3();

        ReviewSaveDto reviewSaveDto3 = new ReviewSaveDto("범죄인줄 알았는데 하 걍 아쉽다",program.getId(),1,reviewTagIdDtoList);


        reviewCUDService.saveReview(user3,reviewSaveDto3);

        Review findReview3=reviewRepository.findByProgramAndUser(program,user3).get();


        Assertions.assertThat(program.getReviewCount()).isEqualTo(3);
        Assertions.assertThat(program.getAverageRating()).isEqualTo(3);



        //user의 평균 별점 변화 확인

        Program program2=makeProgram2();



        ReviewSaveDto reviewSaveDto4 = new ReviewSaveDto("공포는 취향이 아니라서 ㅋ",program2.getId(),2,reviewTagIdDtoList);


        reviewCUDService.saveReview(user,reviewSaveDto4);

        Assertions.assertThat(user.getUserReviewCounts()).isEqualTo(2);
        Assertions.assertThat(user.getAverageRating()).isEqualTo(2.9);



        //좋아요 확인 기능

        reviewCUDService.likeReview(user,findReview3.getId());

        Assertions.assertThat(findReview3.getLikeNumber()).isEqualTo(1);

        reviewCUDService.likeReview(user,findReview3.getId());

        Assertions.assertThat(findReview3.getLikeNumber()).isEqualTo(0);

        reviewCUDService.likeReview(user,findReview3.getId());

        Assertions.assertThat(findReview3.getLikeNumber()).isEqualTo(1);


        String avg = reviewShowProgramDetailService.showUserSpecificRating(user,program.getId());
        Assertions.assertThat(avg).isEqualTo("3.00");



    }
    @Transactional
    User makeUser(){
        User user=User.builder()
                .email("dionisos1998@naver.com")
                .build();

        Genre genre1= genreRepository.findByTmDbGenreId(16L).get();
        Genre genre2= genreRepository.findByTmDbGenreId(35L).get();


        user.addGenre(genre1,true);
        user.addGenre(genre2,false);
        userRepository.save(user);
        return user;
        //코미디를 firstGenre로 두고, 애니메이션을 후 순위로 둠
    }

    @Transactional
    User makeUser2(){
        User user=User.builder()
                .email("dionisos198@gmail.com")
                .build();

        Genre genre1= genreRepository.findByTmDbGenreId(16L).get();
        Genre genre2= genreRepository.findByTmDbGenreId(99L).get();

        user.addGenre(genre1,true);
        user.addGenre(genre2,false);
        userRepository.save(user);
        return user;
        //애니메이션을 firstGenre로 둔 유저, 다큐멘터리 장르를 후순위로 둔 유저
    }

    @Transactional
    User makeUser3(){
        User user=User.builder()
                .email("dionisos198@daum.com")
                .build();

        Genre genre= genreRepository.findByTmDbGenreId(16L).get();//범죄를 first Genre 로 둠
        Genre genre2= genreRepository.findByTmDbGenreId(28L).get();//액션을 둘째 장르로 둠



        user.addGenre(genre,true);
        user.addGenre(genre , false);
        userRepository.save(user);
        return user;
        //범죄를 첫번째 액션을 둘째 장르로 두었다.
    }
    @Transactional
    Program makeProgram(){
        Program program = Program.builder()
                .tmDbProgramId(140L)
                .title("액션과 애니메이션과 코미디가 있는 영화")
                .type(ProgramType.Movie)
                .createdYear("1024")
                .build();

        Genre genre1= genreRepository.findByTmDbGenreId(28L).get();//액션
        Genre genre2= genreRepository.findByTmDbGenreId(16L).get();//애니메이션
        Genre genre3= genreRepository.findByTmDbGenreId(35L).get();//코미디



        program.addGenre(genre1);
        program.addGenre(genre2);
        program.addGenre(genre3);

        programRepository.save(program);

        return program;
    }

    @Transactional
    Program makeProgram2(){
        Program program = Program.builder()
                .tmDbProgramId(150L)
                .title("공포와 미스터리와 SF 가 있는 TV")
                .type(ProgramType.TV)
                .createdYear("1025")
                .build();

        Genre genre1= genreRepository.findByTmDbGenreId(27L).get();//공포
        Genre genre2= genreRepository.findByTmDbGenreId(9648L).get();//미스터리
        Genre genre3= genreRepository.findByTmDbGenreId(878L).get();//SF



        program.addGenre(genre1);
        program.addGenre(genre2);
        program.addGenre(genre3);

        programRepository.save(program);

        return program;
    }
    @Transactional
    List<ReviewTagIdDto> makeReviewTag(){
        ReviewSaveTagDto reviewSaveTagDto1=new ReviewSaveTagDto("잠이와요");


        ReviewSaveTagDto reviewSaveTagDto2=new ReviewSaveTagDto("재밌어요");


        ReviewSaveTagDto reviewSaveTagDto3=new ReviewSaveTagDto("팝콘먹고 싶다");


        ReviewSaveTagDto reviewSaveTagDto4=new ReviewSaveTagDto("보다가 쿵쿵쿵");



        reviewTagService.saveReviewTag(reviewSaveTagDto1);
        reviewTagService.saveReviewTag(reviewSaveTagDto2);
        reviewTagService.saveReviewTag(reviewSaveTagDto3);
        reviewTagService.saveReviewTag(reviewSaveTagDto4);

        List<ReviewTagIdDto> reviewTagIdDtoList=new ArrayList<>();

        reviewTagRepository.findAll().forEach(rt->{
            ReviewTagIdDto reviewTagIdDto=new ReviewTagIdDto(rt.getId());
            reviewTagIdDtoList.add(reviewTagIdDto);
        });

        return reviewTagIdDtoList;

    }




}
