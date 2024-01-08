package tavebalak.OTTify.user.service;

import tavebalak.OTTify.genre.dto.request.GenreUpdateDTO;

public interface UserService {

    void update1stGenre(Long userId, GenreUpdateDTO updateRequestDTO);
    void update2ndGenre(Long userId, GenreUpdateDTO updateRequestDTO);
}
