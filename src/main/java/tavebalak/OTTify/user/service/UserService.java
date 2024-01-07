package tavebalak.OTTify.user.service;

import tavebalak.OTTify.user.dto.Request.UserOttUpdateDTO;
import tavebalak.OTTify.user.dto.Request.UserProfileUpdateDTO;
import tavebalak.OTTify.user.dto.Response.UserOttDTO;
import tavebalak.OTTify.user.dto.Response.UserProfileDTO;

import java.util.List;

public interface UserService {
    UserProfileDTO getUserProfile(Long userId);
    List<UserOttDTO> getUserOTT(Long userId);
    Long updateUserProfile(Long userId, UserProfileUpdateDTO updateRequestDTO);
    Long updateUserOTT(Long userId, List<UserOttUpdateDTO> updateRequestDTO);
}
