package tavebalak.OTTify.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tavebalak.OTTify.common.BaseResponse;
import tavebalak.OTTify.genre.dto.request.GenreUpdateDTO;
import tavebalak.OTTify.user.dto.Request.UserOttUpdateDTO;
import tavebalak.OTTify.user.dto.Response.CommunityListWithSliceInfoDTO;
import tavebalak.OTTify.user.dto.Response.ReviewListWithSliceInfoDTO;
import tavebalak.OTTify.user.dto.Response.UserOttListDTO;
import tavebalak.OTTify.user.dto.Response.UserProfileDTO;
import tavebalak.OTTify.user.dto.Response.UserRoleDto;
import tavebalak.OTTify.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
@Api(tags = {"마이페이지 컨트롤러"})
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "프로필 조회 api", notes = "유저 프로필(닉네임, 프로필 사진)을 조회합니다.")
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<UserProfileDTO> getUserProfile() {
        return BaseResponse.success(userService.getUserProfile());
    }

    @ApiOperation(value = "구독 중인 OTT 조회 api", notes = "유저가 구독 중인 OTT를 조회합니다.")
    @GetMapping("/otts")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<UserOttListDTO> getUserOTT() {
        return BaseResponse.success(userService.getUserOTT());
    }

    @ApiOperation(value = "프로필 수정 api", notes = "유저 프로필(닉네임, 프로필 사진)을 수정합니다. ⚠️ Content-Type를 multipart/form-data로 설정하고 파라미터 별로 MediaType을 설정해 주세요.")
    @ApiResponse(code = 200, message = "성공적으로 프로필이 업데이트 되었습니다.")
    @PatchMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateUserProfile(@RequestParam(required = false) final String nickName, @RequestPart(required = false) final MultipartFile profilePhoto) {
        userService.updateUserProfile(nickName, profilePhoto);
        return BaseResponse.success("성공적으로 프로필이 업데이트 되었습니다.");
    }

    @ApiOperation(value = "구독 중인 OTT 수정 api", notes = "유저가 구독 중인 OTT를 수정합니다.")
    @ApiResponse(code = 200, message = "성공적으로 구독 중인 OTT가 업데이트 되었습니다.")
    @PatchMapping("/otts")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateUserOTT(@RequestBody UserOttUpdateDTO updateRequestDTO) {
        userService.updateUserOTT(updateRequestDTO);
        return BaseResponse.success("성공적으로 구독 중인 OTT가 업데이트 되었습니다.");
    }

    @ApiOperation(value = "1순위 취향 장르 수정 api", notes = "유저의 1순위 취향 장르를 수정합니다.")
    @ApiResponse(code = 200, message = "성공적으로 1순위 장르가 업데이트 되었습니다.")
    @PatchMapping("/1stGenre")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse update1stLikedGenre(@Validated @RequestBody GenreUpdateDTO updateRequestDto) {
        userService.update1stGenre(updateRequestDto);
        return BaseResponse.success("성공적으로 1순위 장르가 업데이트 되었습니다.");
    }

    @ApiOperation(value = "2순위 취향 장르 수정 api", notes = "유저의 2순위 취향 장르를 수정합니다.")
    @ApiResponse(code = 200, message = "성공적으로 2순위 장르가 업데이트 되었습니다.")
    @PatchMapping("/2ndGenre")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse update2ndLikedGenre(@Validated @RequestBody GenreUpdateDTO updateRequestDTO) {
        userService.update2ndGenre(updateRequestDTO);
        return BaseResponse.success("성공적으로 2순위 장르가 업데이트 되었습니다.");
    }

    @ApiOperation(value = "작성 리뷰 조회 api", notes = "유저가 작성한 리뷰를 조회합니다.")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "int", value = "페이지 번호(0부터 시작)", paramType = "query"),
        @ApiImplicitParam(name = "direction", dataType = "String", value = "내림차순과 오름차순", paramType = "query"),
        @ApiImplicitParam(name = "sort", dataType = "String", value = "정렬기준(createdAt, updatedAt)", paramType = "query"),
        @ApiImplicitParam(name = "size", dataType = "int", value = "페이지당 아이템 갯수", paramType = "query")
    })
    @GetMapping("/reviews")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<ReviewListWithSliceInfoDTO> getMyReview(@PageableDefault(
        size = 10,
        sort = "createdAt",
        direction = Sort.Direction.DESC,
        page = 0) Pageable pageable) {
        return BaseResponse.success(userService.getMyReview(pageable));
    }

    @ApiOperation(value = "좋아요한 리뷰 조회 api", notes = "유저가 좋아요한 리뷰를 조회합니다.")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "int", value = "페이지 번호(0부터 시작)", paramType = "query"),
        @ApiImplicitParam(name = "size", dataType = "int", value = "페이지당 아이템 갯수", paramType = "query")
    })
    @GetMapping("/likedReviews")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<ReviewListWithSliceInfoDTO> getLikedReview(@PageableDefault(
        size = 10,
        page = 0) Pageable pageable) {
        return BaseResponse.success(userService.getLikedReview(pageable));
    }

    @ApiOperation(value = "주최한 토론 조회 api", notes = "유저가 주최한 토론을 조회합니다.")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "int", value = "페이지 번호(0부터 시작)", paramType = "query"),
        @ApiImplicitParam(name = "direction", dataType = "String", value = "내림차순과 오름차순", paramType = "query"),
        @ApiImplicitParam(name = "sort", dataType = "String", value = "정렬기준(createdAt, updatedAt)", paramType = "query"),
        @ApiImplicitParam(name = "size", dataType = "int", value = "페이지당 아이템 갯수", paramType = "query")
    })
    @GetMapping("/discussion/hosting")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<CommunityListWithSliceInfoDTO> getHostedDiscussion(@PageableDefault(
        size = 10,
        sort = "createdAt",
        direction = Sort.Direction.DESC,
        page = 0) Pageable pageable) {
        return BaseResponse.success(userService.getHostedDiscussion(pageable));
    }

    @ApiOperation(value = "참여한 토론 조회 api", notes = "유저가 참여한 토론을 조회합니다.")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "int", value = "페이지 번호(0부터 시작)", paramType = "query"),
        @ApiImplicitParam(name = "size", dataType = "int", value = "페이지당 아이템 갯수", paramType = "query")
    })
    @GetMapping("/discussion/participating")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<CommunityListWithSliceInfoDTO> getParticipatedDiscussion(@PageableDefault(
        size = 10,
        page = 0) Pageable pageable) {
        return BaseResponse.success(userService.getParticipatedDiscussion(pageable));
    }

    @ApiOperation(value = "Role 조회 api", notes = "유저의 ROLE을 조회합니다.")
    @GetMapping("/role")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<UserRoleDto> getUserRole() {
        return BaseResponse.success(userService.getUserRole());
    }
}
