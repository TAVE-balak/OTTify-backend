package tavebalak.OTTify.genre.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tavebalak.OTTify.genre.service.GenreService;



@RestController
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @PostMapping("/api/v1/genre/all")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveAllGenre(){
        genreService.saveAllGenre();
    }
}