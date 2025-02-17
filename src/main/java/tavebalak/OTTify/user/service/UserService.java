package tavebalak.OTTify.user.service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import tavebalak.OTTify.genre.dto.request.GenreUpdateDTO;
import tavebalak.OTTify.user.dto.Request.UserOttUpdateDTO;
import tavebalak.OTTify.user.dto.Response.CommunityListWithSliceInfoDTO;
import tavebalak.OTTify.user.dto.Response.ReviewListWithSliceInfoDTO;
import tavebalak.OTTify.user.dto.Response.UserOttListDTO;
import tavebalak.OTTify.user.dto.Response.UserProfileDTO;
import tavebalak.OTTify.user.dto.Response.UserRoleDto;

public interface UserService {

    UserProfileDTO getUserProfile();

    UserOttListDTO getUserOTT();

    void update1stGenre(GenreUpdateDTO updateRequestDTO);

    void update2ndGenre(GenreUpdateDTO updateRequestDTO);

    void updateUserProfile(String nickName, MultipartFile profilePhoto);

    void updateUserOTT(UserOttUpdateDTO updateRequestDTO);

    ReviewListWithSliceInfoDTO getMyReview(Pageable pageable);

    ReviewListWithSliceInfoDTO getLikedReview(Pageable pageable);

    CommunityListWithSliceInfoDTO getHostedDiscussion(Pageable pageable);

    CommunityListWithSliceInfoDTO getParticipatedDiscussion(Pageable pageable);

    UserRoleDto getUserRole();
}
