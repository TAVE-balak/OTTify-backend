package tavebalak.OTTify.user.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tavebalak.OTTify.common.s3.AWSS3Service;
import tavebalak.OTTify.community.dto.response.MyDiscussionDto;
import tavebalak.OTTify.community.entity.Community;
import tavebalak.OTTify.community.repository.CommunityRepository;
import tavebalak.OTTify.community.repository.ReplyRepository;
import tavebalak.OTTify.error.ErrorCode;
import tavebalak.OTTify.error.exception.DuplicateException;
import tavebalak.OTTify.error.exception.NotFoundException;
import tavebalak.OTTify.error.exception.UnauthorizedException;
import tavebalak.OTTify.genre.dto.GenreDTO;
import tavebalak.OTTify.genre.dto.request.GenreUpdateDTO;
import tavebalak.OTTify.genre.entity.Genre;
import tavebalak.OTTify.genre.entity.UserGenre;
import tavebalak.OTTify.genre.repository.GenreRepository;
import tavebalak.OTTify.genre.repository.UserGenreRepository;
import tavebalak.OTTify.oauth.jwt.SecurityUtil;
import tavebalak.OTTify.program.repository.OttRepository;
import tavebalak.OTTify.review.dto.UserReviewRatingListDTO;
import tavebalak.OTTify.review.dto.response.MyReviewDto;
import tavebalak.OTTify.review.entity.Review;
import tavebalak.OTTify.review.repository.ReviewRepository;
import tavebalak.OTTify.user.dto.Request.UserOttUpdateDTO;
import tavebalak.OTTify.user.dto.Response.CommunityListWithSliceInfoDTO;
import tavebalak.OTTify.user.dto.Response.LikedProgramDTO;
import tavebalak.OTTify.user.dto.Response.LikedProgramListDTO;
import tavebalak.OTTify.user.dto.Response.ReviewListWithSliceInfoDTO;
import tavebalak.OTTify.user.dto.Response.UninterestedProgramDTO;
import tavebalak.OTTify.user.dto.Response.UninterestedProgramListDTO;
import tavebalak.OTTify.user.dto.Response.UserOttDTO;
import tavebalak.OTTify.user.dto.Response.UserOttListDTO;
import tavebalak.OTTify.user.dto.Response.UserProfileDTO;
import tavebalak.OTTify.user.dto.Response.UserRoleDto;
import tavebalak.OTTify.user.entity.User;
import tavebalak.OTTify.user.entity.UserSubscribingOTT;
import tavebalak.OTTify.user.repository.LikedCommunityRepository;
import tavebalak.OTTify.user.repository.LikedProgramRepository;
import tavebalak.OTTify.user.repository.LikedReplyRepository;
import tavebalak.OTTify.user.repository.LikedReviewRepository;
import tavebalak.OTTify.user.repository.UninterestedProgramRepository;
import tavebalak.OTTify.user.repository.UserRepository;
import tavebalak.OTTify.user.repository.UserSubscribingOttRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserGenreRepository userGenreRepository;
    private final UserSubscribingOttRepository userSubscribingOttRepository;
    private final OttRepository ottRepository;
    private final ReviewRepository reviewRepository;
    private final LikedProgramRepository likedProgramRepository;
    private final LikedReviewRepository likedReviewRepository;
    private final LikedCommunityRepository likedCommunityRepository;
    private final LikedReplyRepository likedReplyRepository;
    private final UninterestedProgramRepository uninterestedProgramRepository;
    private final GenreRepository genreRepository;
    private final CommunityRepository communityRepository;
    private final ReplyRepository replyRepository;
    private final AWSS3Service awss3Service;

    private static final double RATING_ZERO_DOT_FIVE = 0.5;
    private static final double RATING_ONE = 1.0;
    private static final double RATING_ONE_DOT_FIVE = 1.5;
    private static final double RATING_TWO = 2.0;
    private static final double RATING_TWO_DOT_FIVE = 2.5;
    private static final double RATING_THREE = 3.0;
    private static final double RATING_THREE_DOT_FIVE = 3.5;
    private static final double RATING_FOUR = 4.0;
    private static final double RATING_FOUR_DOT_FIVE = 4.5;
    private static final double RATING_FIVE = 5.0;

    @Override
    public UserProfileDTO getUserProfile() {
        User user = getUser();
        Long userId = user.getId();

        // 1순위 & 2순위 장르 가져오기
        UserGenre firstUserGenre = userGenreRepository.find1stGenreByUserIdFetchJoin(userId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_FIRST_GENRE_NOT_FOUND));

        GenreDTO firstGenre = new GenreDTO(firstUserGenre);

        List<GenreDTO> secondGenre = userGenreRepository.find2ndGenreByUserIdFetchJoin(userId)
            .stream()
            .map(ug -> new GenreDTO(ug))
            .collect(Collectors.toList());

        // 별점 리스트 가져오기
        HashMap<Double, Integer> ratingList = new HashMap<Double, Integer>();
        ArrayList<Double> ratingSet = new ArrayList<>(
            Arrays.asList(RATING_ZERO_DOT_FIVE, RATING_ONE, RATING_ONE_DOT_FIVE, RATING_TWO, RATING_TWO_DOT_FIVE, RATING_THREE, RATING_THREE_DOT_FIVE, RATING_FOUR, RATING_FOUR_DOT_FIVE, RATING_FIVE));

        List<Double> reviewRatingList = new ArrayList<>();
        reviewRepository.findByUserId(userId).stream()
            .forEach(r -> {
                reviewRatingList.add(r.getRating());
            });
        for (Double r : ratingSet) {
            if (Collections.frequency(reviewRatingList, r) == 0) {
                ratingList.put(r, 0);
            } else {
                ratingList.put(r, Collections.frequency(reviewRatingList, r));
            }
        }

        UserReviewRatingListDTO userReviewRatingListDTO = UserReviewRatingListDTO.builder()
            .totalCnt(reviewRatingList.size())
            .pointFiveCnt(ratingList.get(RATING_ZERO_DOT_FIVE))
            .oneCnt(ratingList.get(RATING_ONE))
            .oneDotFiveCnt(ratingList.get(RATING_ONE_DOT_FIVE))
            .twoCnt(ratingList.get(RATING_TWO))
            .twoDotFiveCnt(ratingList.get(RATING_TWO_DOT_FIVE))
            .threeCnt(ratingList.get(RATING_THREE))
            .threeDotFiveCnt(ratingList.get(RATING_THREE_DOT_FIVE))
            .fourCnt(ratingList.get(RATING_FOUR))
            .fourDotFiveCnt(ratingList.get(RATING_FOUR_DOT_FIVE))
            .fiveCnt(ratingList.get(RATING_FIVE))
            .build();

        // OTT 리스트 가져오기
        List<UserOttDTO> userOttDTOList = userSubscribingOttRepository.findByUserIdFetchJoin(userId)
            .stream()
            .map((UserSubscribingOTT uso) -> new UserOttDTO(uso))
            .collect(Collectors.toList());
        UserOttListDTO userOttListDTO = UserOttListDTO.builder()
            .totalCnt(userOttDTOList.size())
            .ottList(userOttDTOList)
            .build();

        // 보고싶은 프로그램 가져오기
        List<LikedProgramDTO> likedProgramDTOList = likedProgramRepository.findByUserIdFetchJoin(
                userId).stream()
            .map(p -> new LikedProgramDTO(p.getId(), p.getProgram().getPosterPath()))
            .collect(Collectors.toList());
        LikedProgramListDTO likedProgramListDTO = LikedProgramListDTO.builder()
            .totalCnt(likedProgramDTOList.size())
            .likedProgramList(likedProgramDTOList)
            .build();

        // 관심없는 프로그램 가져오기
        List<UninterestedProgramDTO> uninterestedProgramDTOList = uninterestedProgramRepository.findByUserIdFetchJoin(
                userId).stream()
            .map(p -> new UninterestedProgramDTO(p.getId(), p.getProgram().getPosterPath()))
            .collect(Collectors.toList());
        UninterestedProgramListDTO uninterestedProgramListDTO = UninterestedProgramListDTO.builder()
            .totalCnt(uninterestedProgramDTOList.size())
            .uninterestedProgramList(uninterestedProgramDTOList)
            .build();

        return UserProfileDTO.builder()
            .profilePhoto(user.getProfilePhoto())
            .nickName(user.getNickName())
            .grade(user.getGrade())
            .email(user.getEmail())
            .averageRating(user.getAverageRating())
            .firstGenre(firstGenre)
            .secondGenre(secondGenre)
            .ott(userOttListDTO)
            .ratingList(userReviewRatingListDTO)
            .likedProgram(likedProgramListDTO)
            .uninterestedProgram(uninterestedProgramListDTO)
            .build();
    }

    @Override
    public UserOttListDTO getUserOTT() {
        User user = getUser();
        Long userId = user.getId();

        List<UserOttDTO> userOttDTOList = userSubscribingOttRepository.findByUserIdFetchJoin(userId).stream()
            .map((UserSubscribingOTT uso) -> new UserOttDTO(uso))
            .collect(Collectors.toList());
        return UserOttListDTO.builder()
            .totalCnt(userOttDTOList.size())
            .ottList(userOttDTOList)
            .build();
    }

    @Override
    @Transactional
    public void update1stGenre(GenreUpdateDTO updateRequestDTO) {
        User user = getUser();
        Long userId = user.getId();

        UserGenre userGenre = userGenreRepository.findByUserIdAndIsFirst(userId, true)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_FIRST_GENRE_NOT_FOUND));

        Genre genre = genreRepository.findById(updateRequestDTO.getGenreId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.GENRE_NOT_FOUND));

        userGenre.changeGenre(genre);
    }

    @Override
    @Transactional
    public void update2ndGenre(GenreUpdateDTO updateRequestDTO) {
        User user = getUser();
        Long userId = user.getId();

        // req로 들어온 id 값이 유효한 장르 id인지 확인
        Genre genre = genreRepository.findById(updateRequestDTO.getGenreId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.GENRE_NOT_FOUND));

        // 조회된 UserGenre가 있을 경우 삭제 & 없을 경우 저장
        userGenreRepository.findByGenreIdAndUserIdAndIsFirst(genre.getId(), userId, false)
            .ifPresentOrElse(
                ug -> userGenreRepository.delete(ug),
                () -> userGenreRepository.save(
                    UserGenre.builder()
                        .genre(genre)
                        .user(user)
                        .build())
            );
    }

    @Override
    @Transactional

    public void updateUserProfile(String nickName, MultipartFile profilePhoto) {
        User user = getUser();

        if (nickName != null) {
            checkNicknameDuplication(user, nickName);
            user.changeNickName(nickName);
        }

        // 프로필 사진이 존재하고 유효한 사진인 경우 프로필 사진 변경
        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            // 이전 프로필 사진이 S3에 존재한다면 S3에서 삭제
            awss3Service.delete(user.getProfilePhoto());

            String newPhotoUrl = awss3Service.upload(profilePhoto, "profile-images");
            user.changeProfilePhoto(newPhotoUrl);
        }

        userRepository.save(user);
    }

    public void checkNicknameDuplication(User user, String nickName) {
        if (userRepository.existsByNickName(nickName) && !Objects.equals(user.getNickName(), nickName)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_NICKNAME);
        }
    }

    @Override
    @Transactional
    public void updateUserOTT(UserOttUpdateDTO updateRequestDTO) {
        User user = getUser();
        Long userId = user.getId();

        // 이전에 구독 중이던 ott 리스트
        List<Long> preSubscribingOttList = userSubscribingOttRepository.findByUserIdFetchJoin(
                userId).stream()
            .map((UserSubscribingOTT t) -> t.getOtt().getId())
            .collect(Collectors.toList());

        // 현재 구독 중인 ott 리스트
        List<Long> nowSubscribingOttList = updateRequestDTO.getOttList();

        if (!preSubscribingOttList.isEmpty()) { // 이전 구독 중인 OTT가 있는 경우
            // 삭제 Otts - 이전 리스트에는 있는데 현재 리스트에는 없는 경우
            List<Long> deleteOtts = preSubscribingOttList.stream()
                .filter(ott -> !nowSubscribingOttList.contains(ott))
                .collect(Collectors.toList());
            userSubscribingOttRepository.deleteAllByIdInQuery(deleteOtts, userId);

            // 추가 otts - 이전 리스트에는 없는데 현재 리스트에는 있는 경우
            List<Long> insertOtts = nowSubscribingOttList.stream()
                .filter(ott -> !preSubscribingOttList.contains(ott))
                .collect(Collectors.toList());

            insertOtts.stream()
                .forEach(ott -> {
                    UserSubscribingOTT subscribingOTT = UserSubscribingOTT.create(
                        user,
                        ottRepository.findById(ott)
                            .orElseThrow(() -> new NotFoundException(ErrorCode.OTT_NOT_FOUND))
                    );
                    userSubscribingOttRepository.save(subscribingOTT);
                });
        } else { // 이전 구독 중인 OTT가 없는 경우
            nowSubscribingOttList
                .stream()
                .forEach(ott -> {
                    UserSubscribingOTT subscribingOTT = UserSubscribingOTT.create(
                        user,
                        ottRepository.findById(ott)
                            .orElseThrow(() -> new NotFoundException(ErrorCode.OTT_NOT_FOUND))
                    );
                    userSubscribingOttRepository.save(subscribingOTT);
                });
        }
    }

    @Override
    public ReviewListWithSliceInfoDTO getMyReview(Pageable pageable) {
        User user = getUser();
        Long userId = user.getId();

        Slice<MyReviewDto> reviewList = reviewRepository.findByUserIdOrderByCreatedAt(userId, pageable)
            .map(r -> createReviewDto(r));

        return new ReviewListWithSliceInfoDTO(reviewList.getContent(), reviewList.isLast());
    }

    @Override
    public ReviewListWithSliceInfoDTO getLikedReview(Pageable pageable) {
        User user = getUser();
        Long userId = user.getId();

        Slice<MyReviewDto> reviewList = likedReviewRepository.findReviewByUserId(userId, pageable)
            .map(r -> createReviewDto(r));

        return new ReviewListWithSliceInfoDTO(reviewList.getContent(), reviewList.isLast());
    }

    @Override
    public CommunityListWithSliceInfoDTO getHostedDiscussion(Pageable pageable) {
        User user = getUser();
        Long userId = user.getId();

        Slice<MyDiscussionDto> discussionList = communityRepository.findByUserId(userId, pageable)
            .map(d -> createDiscussionDto(d));

        return new CommunityListWithSliceInfoDTO(discussionList.getContent(), discussionList.isLast());
    }

    @Override
    public CommunityListWithSliceInfoDTO getParticipatedDiscussion(Pageable pageable) {
        User user = getUser();
        Long userId = user.getId();

        Slice<MyDiscussionDto> discussionList = replyRepository.findAllCommunityByUserId(userId, pageable)
            .map(d -> createDiscussionDto(d));

        return new CommunityListWithSliceInfoDTO(discussionList.getContent(), discussionList.isLast());
    }

    private MyDiscussionDto createDiscussionDto(Community d) {
        int likeCnt = likedCommunityRepository.countByCommunityId(d.getId());
        int replyCnt = likedReplyRepository.countByCommunityId(d.getId());

        return MyDiscussionDto.builder()
            .discussionId(d.getId())
            .createdDate(d.getCreatedAt())
            .programTitle(d.getProgram().getTitle())
            .discussionTitle(d.getTitle())
            .content(d.getContent())
            .imgUrl(d.getImageUrl())
            .likeCnt(likeCnt)
            .replyCnt(replyCnt)
            .build();
    }

    private MyReviewDto createReviewDto(Review r) {
        // 리뷰에 달린 reviewTags 가져오기
        List<String> reviewTagNames = r.getReviewReviewTags().stream()
            .map(reviewReviewTag -> reviewReviewTag.getReviewTag().getName())
            .collect(Collectors.toList());

        return MyReviewDto.builder()
            .reviewId(r.getId())
            .createdDate(r.getCreatedAt())
            .userProfilePhoto(r.getUser().getProfilePhoto())
            .userNickName(r.getUser().getNickName())
            .programTitle(r.getProgram().getTitle())
            .reviewRating(r.getRating())
            .reviewTagNames(reviewTagNames)
            .content(r.getContent())
            .likeCnt(r.getLikeCounts())
            .build();
    }

    @Override
    public UserRoleDto getUserRole() {
        User user = getUser();
        return new UserRoleDto(user.getRole());
    }

    private User getUser() {
        return userRepository.findByEmail(SecurityUtil.getCurrentEmail().get())
            .orElseThrow(() -> new UnauthorizedException(ErrorCode.UNAUTHORIZED));
    }
}
