package tavebalak.OTTify.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tavebalak.OTTify.common.BaseResponse;
import tavebalak.OTTify.error.exception.NotFoundException;
import tavebalak.OTTify.user.dto.Response.UserOttDTO;
import tavebalak.OTTify.user.dto.Request.UserOttUpdateDTO;
import tavebalak.OTTify.user.dto.Response.UserOttListDTO;
import tavebalak.OTTify.user.dto.Response.UserProfileDTO;
import tavebalak.OTTify.user.dto.Request.UserProfileUpdateDTO;
import tavebalak.OTTify.user.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<UserProfileDTO> getUserProfile(@PathVariable("id") Long userId) {
        return BaseResponse.success(userService.getUserProfile(userId));
    }

    @GetMapping("/{id}/otts")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<UserOttListDTO> getUserOTT(@PathVariable("id") Long userId) {
        return BaseResponse.success(userService.getUserOTT(userId));
    }

    @PatchMapping("/{id}/profile")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Long> updateUserProfile(@PathVariable("id") Long userId, @Validated @RequestBody UserProfileUpdateDTO updateRequestDTO) {
        return BaseResponse.success(userService.updateUserProfile(userId, updateRequestDTO));
    }

    @PatchMapping("/{id}/otts")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Long> updateUserOTT(@PathVariable("id") Long userId, @RequestBody List<UserOttUpdateDTO> updateRequestDTO) {
        return BaseResponse.success(userService.updateUserOTT(userId, updateRequestDTO));
    }
}
