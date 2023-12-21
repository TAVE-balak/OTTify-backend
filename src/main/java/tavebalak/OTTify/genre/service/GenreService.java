package tavebalak.OTTify.genre.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import tavebalak.OTTify.genre.dto.OpenApiGenreListResponseDto;
import tavebalak.OTTify.genre.entity.Genre;
import tavebalak.OTTify.genre.repository.GenreRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class GenreService {
    private final GenreRepository genreRepository;
    private final WebClient webClient;

    public void saveAllGenre(){
        OpenApiGenreListResponseDto movieGenreDto=getGenreListFrom("movie");
        saveGenre(movieGenreDto);
        OpenApiGenreListResponseDto tvGenreDto=getGenreListFrom("tv");
        saveGenre(tvGenreDto);
    }

    private OpenApiGenreListResponseDto getGenreListFrom(String type){
        return webClient.get()
                .uri("/genre/"+type+"/list?language=ko")
                .retrieve()
                .bodyToMono(OpenApiGenreListResponseDto.class)
                .block();
    }
    private void saveGenre(OpenApiGenreListResponseDto openApiGenreListResponseDto){
        openApiGenreListResponseDto.getGenres().stream().map(gd-> Genre.builder()
                        .tmDbGenreId(gd.getId())
                        .name(gd.getName())
                        .build())
                .forEach(g->{
                    if(!genreRepository.existsByTmDbGenreId(g.getTmDbGenreId())){
                        genreRepository.save(g);
                    }
                });
    }
}
