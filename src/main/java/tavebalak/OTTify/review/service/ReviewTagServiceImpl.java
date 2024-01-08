package tavebalak.OTTify.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tavebalak.OTTify.review.dto.reviewtagRequest.ReviewSaveTagDto;
import tavebalak.OTTify.review.dto.reviewtagResponse.ReviewTagInfo;
import tavebalak.OTTify.review.dto.reviewtagResponse.ReviewTagListResponseDto;
import tavebalak.OTTify.review.entity.ReviewTag;
import tavebalak.OTTify.review.repository.ReviewTagRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewTagServiceImpl implements ReviewTagService{
    private final ReviewTagRepository reviewTagRepository;
    @Override
    @Transactional
    public void saveReviewTag(ReviewSaveTagDto reviewSaveTagDto){
        ReviewTag reviewTag = ReviewTag.builder()
                .name(reviewSaveTagDto.getName())
                .build();

        reviewTagRepository.save(reviewTag);
    }

    @Override

    public ReviewTagListResponseDto showReviewTagList(){
        List<ReviewTagInfo> reviewTagInfoList =reviewTagRepository.findAll().stream().map(ReviewTagInfo::new).collect(Collectors.toList());


        return new ReviewTagListResponseDto(reviewTagInfoList);
    }
    @Override
    @Transactional
    public void basicReviewTagSave(){
        ReviewTag reviewTag1= makeReviewTag("ost가 좋아요");//ost가 좋아요
        ReviewTag reviewTag2= makeReviewTag("스토리 내용이 개연성 있어요"); //스토리 내용이 개연성 있어요
        ReviewTag reviewTag3= makeReviewTag("극장에서 또 보고 싶어요"); //극장에서 또 보고 싶어요.
        ReviewTag reviewTag4 = makeReviewTag("노잼이에요"); //노잼이에요.
        ReviewTag reviewTag5 = makeReviewTag("돈이 아까워요"); //돈이아까워요.
        ReviewTag reviewTag6 = makeReviewTag("반전이 있어요"); //반전이 있어요.
        ReviewTag reviewTag7= makeReviewTag("보호자 동반 필요");//보호자 동반 필요
        ReviewTag reviewTag8= makeReviewTag("손수건 필요"); //손수건 필요
        ReviewTag reviewTag9= makeReviewTag("시간 가는줄 몰랐어요"); //시간 가는줄 몰랐어요
        ReviewTag reviewTag10 = makeReviewTag("심장 질환자 관람 유의"); //심장 질환자 관람 유의
        ReviewTag reviewTag11 = makeReviewTag("연기가 좋아요"); //연기가 좋아요
        ReviewTag reviewTag12 = makeReviewTag("연인이랑 같이 보기 좋아요"); //연인이랑 같이 보기 좋아요
        ReviewTag reviewTag13 = makeReviewTag("팝콘 필수에요"); //팝콘 필수에요

        List<ReviewTag> reviewTags = Arrays.asList(
                reviewTag1,
                reviewTag2,
                reviewTag3,
                reviewTag4,
                reviewTag5,
                reviewTag6,
                reviewTag7,
                reviewTag8,
                reviewTag9,
                reviewTag10,
                reviewTag11,
                reviewTag12,
                reviewTag13
        );

        reviewTagRepository.saveAll(reviewTags);


    }
    private ReviewTag makeReviewTag(String name){
        ReviewTag reviewTag = ReviewTag.builder()
                .name(name)
                .build();

        return reviewTag;
    }
}
