package tavebalak.OTTify.common.component;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import tavebalak.OTTify.genre.service.GenreService;
import tavebalak.OTTify.review.service.ReviewTagService;

@Component
@RequiredArgsConstructor
public class OpenApiDataLoader implements ApplicationRunner {
    private final GenreService genreService;
    private final ReviewTagService reviewTagService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        genreService.saveAllGenre();
        reviewTagService.basicReviewTagSave();
    }
}
