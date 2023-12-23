package tavebalak.OTTify.common.component;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import tavebalak.OTTify.genre.service.GenreService;

@Component
@RequiredArgsConstructor
public class OpenApiDataLoader implements ApplicationRunner {
    private final GenreService genreService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        genreService.saveAllGenre();
    }
}
