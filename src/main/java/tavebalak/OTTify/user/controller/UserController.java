package tavebalak.OTTify.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tavebalak.OTTify.common.BaseResponse;
import tavebalak.OTTify.genre.dto.request.GenreUpdateDTO;
import tavebalak.OTTify.user.service.UserServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserServiceImpl userService;

    @PatchMapping("/{id}/1stGenre")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse update1stLikedGenre(@PathVariable("id") Long userId, @Validated @RequestBody GenreUpdateDTO updateRequestDto) {
        userService.update1stGenre(userId, updateRequestDto);
        return BaseResponse.success("성공적으로 1순위 장르가 업데이트 되었습니다.");
    }

    @PatchMapping("/{id}/2ndGenre")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse update2ndLikedGenre(@PathVariable("id") Long userId, @Validated @RequestBody GenreUpdateDTO updateRequestDTO) {
        userService.update2ndGenre(userId, updateRequestDTO);
        return BaseResponse.success("성공적으로 2순위 장르가 업데이트 되었습니다.");
    }
}
