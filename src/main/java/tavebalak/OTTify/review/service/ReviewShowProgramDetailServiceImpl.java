package tavebalak.OTTify.review.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tavebalak.OTTify.error.ErrorCode;
import tavebalak.OTTify.error.exception.NotFoundException;
import tavebalak.OTTify.genre.entity.Genre;
import tavebalak.OTTify.genre.repository.ProgramGenreRepository;
import tavebalak.OTTify.genre.repository.UserGenreRepository;
import tavebalak.OTTify.oauth.jwt.SecurityUtil;
import tavebalak.OTTify.program.entity.Program;
import tavebalak.OTTify.program.repository.ProgramRepository;
import tavebalak.OTTify.review.dto.reviewresponse.ReviewProgramResponseDto;
import tavebalak.OTTify.review.dto.reviewresponse.ReviewResponseDtoList;
import tavebalak.OTTify.review.entity.Review;
import tavebalak.OTTify.review.repository.ReviewRepository;
import tavebalak.OTTify.review.repository.ReviewReviewTagRepository;
import tavebalak.OTTify.review.repository.ReviewTagRepository;
import tavebalak.OTTify.user.entity.User;
import tavebalak.OTTify.user.repository.LikedReviewRepository;
import tavebalak.OTTify.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewShowProgramDetailServiceImpl implements ReviewShowProgramDetailService{
    private final ReviewRepository reviewRepository;
    private final ProgramRepository programRepository;
    private final UserGenreRepository userGenreRepository;
    private final ReviewTagRepository reviewTagRepository;
    private final ReviewReviewTagRepository reviewReviewTagRepository;
    private final ProgramGenreRepository programGenreRepository;
    private final LikedReviewRepository likedReviewRepository;
    private final UserRepository userRepository;



    //상세페이지에서 4개의 장르 상관없는 좋아요 순 리뷰를 보여줍니다

    @Override
    public ReviewResponseDtoList show4Review(Long programId){

        Program program=programRepository.findById(programId).orElseThrow(()->new NotFoundException(ErrorCode.PROGRAM_NOT_FOUND));

        List<ReviewProgramResponseDto> reviewProgramResponseDtoList = new ArrayList<>();

        AtomicBoolean userHaveReview = new AtomicBoolean(false);

        //로그인한 사용자가 만약 자신의 리뷰를 남겼다면 그 리뷰를 첫번째로 보여줍니다.

        userRepository.findByEmail(SecurityUtil.getCurrentEmail().get()).ifPresent(user -> {
            reviewRepository.findByProgramAndUser(program,user).ifPresent(review -> {
                reviewProgramResponseDtoList.add(makeReviewDto(review));
                userHaveReview.set(true);
            });
        });

        //로그인한 사용자가 자신의 리뷰를 남겼을때는 3개를 추가하고
        if(userHaveReview.get()){
            reviewRepository.findTop3ByProgramOrderByLikeNumberDesc(program).forEach(review -> {
                reviewProgramResponseDtoList.add(makeReviewDto(review));
            });
        }

        //그렇지 않으면 4개를 추가합니다.
        else{
            reviewRepository.findTop4ByProgramOrderByLikeNumberDesc(program).forEach(review -> {
                reviewProgramResponseDtoList.add(makeReviewDto(review));
            });
        }

        return new ReviewResponseDtoList(reviewProgramResponseDtoList);
    }


    //모든 장르 상관 없는 리뷰 리스트를 Slice로  보여줍니다.

    @Override
    public Slice<ReviewProgramResponseDto> showReviewList(Long programId, Pageable pageable){
        Program program=programRepository.findById(programId).orElseThrow(()->new NotFoundException(ErrorCode.PROGRAM_NOT_FOUND));

        Slice<Review> reviewSlice = reviewRepository.findByProgram(program,pageable);

        return reviewSlice.map(r ->  makeReviewDto(r));
    }



    //상세페이지에서 4개의 장르 상관있는 User 에 특화된 좋아요 순 리뷰를 보여줍니다.

    @Override

    public ReviewResponseDtoList show4UserSpecificReviewList(User user, Long programId){

        Program program=programRepository.findById(programId).orElseThrow(()->new NotFoundException(ErrorCode.PROGRAM_NOT_FOUND));

        Genre usersFirstGenre = userGenreRepository.findByUserAndIsFirst(user,true).orElseThrow(()->new NotFoundException(ErrorCode.USER_FIRST_GENRE_NOT_FOUND)).getGenre();

        List<ReviewProgramResponseDto> reviewProgramResponseDtoList = reviewRepository.findUserSpecific4ByProgramAndGenreOrderByLikeNumberDesc(program, usersFirstGenre.getName(), PageRequest.of(0,4)).stream().map(r-> makeReviewDto(r)).collect(Collectors.toList());

        return new ReviewResponseDtoList(reviewProgramResponseDtoList);

    }

    // 유저의 first genre 를 기반으로 리뷰리스트를 slice로 보여줍니다

    @Override
    public Slice<ReviewProgramResponseDto> showUserSpecificReviewList(User user, Long programId, Pageable pageable){

        Program program=programRepository.findById(programId).orElseThrow(()->new NotFoundException(ErrorCode.PROGRAM_NOT_FOUND));

        Genre usersFirstGenre = userGenreRepository.findByUserAndIsFirst(user,true).orElseThrow(()->new NotFoundException(ErrorCode.USER_FIRST_GENRE_NOT_FOUND)).getGenre();

        Slice<Review> reviewSlice = reviewRepository.findUserSpecificByProgramAndGenre(program, usersFirstGenre.getName(),pageable);


        return reviewSlice.map(r->makeReviewDto(r));

    }


    //user 의 first genre 에 맞춘 평점을 보여줍니다. 지금 현재는 컨트롤러에서 사용하고 있지만 나중에 프로그램 상세 페이지에서 같이 넣으려고 합니다

    @Override
    public String showUserSpecificRating(User user,Long programId){
        Program program=programRepository.findById(programId).orElseThrow(()->new NotFoundException(ErrorCode.PROGRAM_NOT_FOUND));

        Genre usersFirstGenre = userGenreRepository.findByUserAndIsFirst(user,true).orElseThrow(()->new NotFoundException(ErrorCode.USER_FIRST_GENRE_NOT_FOUND)).getGenre();

        long userSpecificGenreCount = reviewRepository.countByMyGenreName(usersFirstGenre.getName(),program);


        Double sumRating = reviewRepository.sumReviewRatingByGenreName(usersFirstGenre.getName(),program);

        double userSpecificReviewRatingSum = (sumRating != null) ? sumRating : 0.0;

        double avg;

        if(userSpecificGenreCount==0){
            avg=0;
        }
        else{
            avg = userSpecificReviewRatingSum /userSpecificGenreCount;
        }

        String avgString = String.format("%.2f", avg);

        return avgString;
    }

    //리뷰를 이용해 리뷰 DTO를 만듭니다

    private ReviewProgramResponseDto makeReviewDto(Review review){

        List<String> reviewTagNames = reviewReviewTagRepository.findByReviewWithReviewTagFetch(review.getId())
                .stream().map(reviewReviewTag -> reviewReviewTag.getReviewTag().getName())
                .collect(Collectors.toList());


        ReviewProgramResponseDto reviewProgramResponseDto = ReviewProgramResponseDto.builder()
                .reviewTagNames(reviewTagNames)
                .createdDateTime(review.getCreatedAt())
                .like(review.getLikeNumber())
                .ratings(review.getRating())
                .contents(review.getContent())
                .userNickName(review.getUser().getNickName())
                .userPosterPath(review.getUser().getProfilePhoto())
                .reviewId(review.getId())
                .build();

        return reviewProgramResponseDto;
    }


}
