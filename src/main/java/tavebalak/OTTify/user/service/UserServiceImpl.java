package tavebalak.OTTify.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tavebalak.OTTify.error.ErrorCode;
import tavebalak.OTTify.error.exception.DuplicateException;
import tavebalak.OTTify.error.exception.NotFoundException;
import tavebalak.OTTify.genre.dto.GenreDTO;
import tavebalak.OTTify.genre.entity.UserGenre;
import tavebalak.OTTify.genre.repository.UserGenreRepository;
import tavebalak.OTTify.program.repository.OttRepository;
import tavebalak.OTTify.review.dto.UserReviewRatingListDTO;
import tavebalak.OTTify.review.repository.ReviewRepository;
import tavebalak.OTTify.user.dto.Request.UserOttUpdateDTO;
import tavebalak.OTTify.user.dto.Request.UserProfileUpdateDTO;
import tavebalak.OTTify.user.dto.Response.*;
import tavebalak.OTTify.user.entity.User;
import tavebalak.OTTify.user.entity.UserSubscribingOTT;
import tavebalak.OTTify.user.repository.LikedProgramRepository;
import tavebalak.OTTify.user.repository.UninterestedProgramRepository;
import tavebalak.OTTify.user.repository.UserRepository;
import tavebalak.OTTify.user.repository.UserSubscribingOttRepository;

import java.util.*;
import java.util.stream.Collectors;

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
    private final UninterestedProgramRepository uninterestedProgramRepository;

    @Override
    public UserProfileDTO getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        // 1순위 & 2순위 장르 가져오기
        UserGenre firstUserGenre = userGenreRepository.find1stGenreByUserIdFetchJoin(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        GenreDTO firstGenre = new GenreDTO(firstUserGenre);

        List<GenreDTO> secondGenre = userGenreRepository.find2ndGenreByUserIdFetchJoin(userId).stream()
                .map(ug -> new GenreDTO(ug))
                .collect(Collectors.toList());

        // 별점 리스트 가져오기
        HashMap<Double, Integer> ratingList = new HashMap<Double, Integer>();
        ArrayList<Double> ratingSet = new ArrayList<>(Arrays.asList(0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0));

        List<Double> reviewRatingList = new ArrayList<>();
        reviewRepository.findByUserId(userId).stream()
                .forEach(r -> {
                    reviewRatingList.add(r.getRating());
                });
        for (Double r : ratingSet) {
            if (Collections.frequency(reviewRatingList, r) == 0) {
                ratingList.put(r, 0);
            }
            else {
                ratingList.put(r, Collections.frequency(reviewRatingList, r));
            }
        }

        UserReviewRatingListDTO userReviewRatingListDTO = UserReviewRatingListDTO.builder()
                .totalCnt(reviewRatingList.size())
                .pointFiveCnt(ratingList.get(0.5))
                .oneCnt(ratingList.get(1.0))
                .oneDotFiveCnt(ratingList.get(1.5))
                .twoCnt(ratingList.get(2.0))
                .twoDotFiveCnt(ratingList.get(2.5))
                .threeCnt(ratingList.get(3.0))
                .threeDotFiveCnt(ratingList.get(3.5))
                .fourCnt(ratingList.get(4.0))
                .fourDotFiveCnt(ratingList.get(4.5))
                .fiveCnt(ratingList.get(5.0))
                .build();

        // OTT 리스트 가져오기
        List<UserOttDTO> userOttDTOList = userSubscribingOttRepository.findByUserIdFetchJoin(userId).stream()
                .map((UserSubscribingOTT uso) -> new UserOttDTO(uso))
                .collect(Collectors.toList());
        UserOttListDTO userOttListDTO = UserOttListDTO.builder()
                .totalCnt(userOttDTOList.size())
                .ottList(userOttDTOList)
                .build();

        // 보고싶은 프로그램 가져오기
        List<LikedProgramDTO> likedProgramDTOList = likedProgramRepository.findByUserIdFetchJoin(userId).stream()
                .map(p -> new LikedProgramDTO(p.getId(), p.getProgram().getPosterPath()))
                .collect(Collectors.toList());
        LikedProgramListDTO likedProgramListDTO = LikedProgramListDTO.builder()
                .totalCnt(likedProgramDTOList.size())
                .likedProgramList(likedProgramDTOList)
                .build();

        // 관심없는 프로그램 가져오기
        List<UninterestedProgramDTO> uninterestedProgramDTOList = uninterestedProgramRepository.findByUserIdFetchJoin(userId).stream()
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
    public UserOttListDTO getUserOTT(Long userId) {
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
    public Long updateUserProfile(Long userId, UserProfileUpdateDTO updateRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        // 닉네임 중복 여부 검증
        checkNicknameDuplication(userId, updateRequestDTO);

        user.changeNickName(updateRequestDTO.getNickName());
        user.changeProfilePhoto(updateRequestDTO.getProfilePhoto());

        return userRepository.save(user).getId();
    }

    public void checkNicknameDuplication(Long userId, UserProfileUpdateDTO updateRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        if (userRepository.existsByNickName(updateRequestDTO.getNickName()) && !Objects.equals(user.getNickName(), updateRequestDTO.getNickName())) {
            throw new DuplicateException(ErrorCode.DUPLICATE_NICKNAME);
        }
    }

    @Override
    @Transactional
    public Long updateUserOTT(Long userId, List<UserOttUpdateDTO> updateRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        // 이전에 구독 중이던 ott 리스트
        List<Long> preSubscribingOttList = userSubscribingOttRepository.findByUserIdFetchJoin(userId).stream()
                .map((UserSubscribingOTT t) -> t.getOtt().getId())
                .collect(Collectors.toList());

        // 현재 구독 중인 ott 리스트
        List<Long> nowSubscribingOttList = updateRequestDTO.stream()
                .map(UserOttUpdateDTO::getId)
                .collect(Collectors.toList());

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

        return userId;
    }
}
