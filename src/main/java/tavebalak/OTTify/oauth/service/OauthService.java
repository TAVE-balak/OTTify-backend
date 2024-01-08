package tavebalak.OTTify.oauth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tavebalak.OTTify.common.constant.SocialType;
import tavebalak.OTTify.error.exception.NotFoundException;
import tavebalak.OTTify.error.exception.UnauthorizedException;
import tavebalak.OTTify.genre.entity.UserGenre;
import tavebalak.OTTify.genre.repository.GenreRepository;
import tavebalak.OTTify.genre.repository.UserGenreRepository;
import tavebalak.OTTify.oauth.dto.SignUpInfoDto;
import tavebalak.OTTify.oauth.redis.RefreshTokenService;
import tavebalak.OTTify.program.repository.OttRepository;
import tavebalak.OTTify.user.entity.User;
import tavebalak.OTTify.user.entity.UserSubscribingOTT;
import tavebalak.OTTify.user.repository.UserRepository;
import tavebalak.OTTify.user.repository.UserSubscribingOttRepository;

import java.net.URI;
import java.util.List;

import static tavebalak.OTTify.error.ErrorCode.ENTITY_NOT_FOUND;
import static tavebalak.OTTify.error.ErrorCode.SIGNIN_EXPIRED;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OauthService {
    private final UserRepository userRepository;
    private final OttRepository ottRepository;
    private final UserSubscribingOttRepository userSubscribingOTTRepository;
    private final UserGenreRepository userGenreRepository;
    private final GenreRepository genreRepository;
    private final RefreshTokenService tokenService;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;


    @Transactional
    public void saveCode(User user, String code) {
        user.changeCode(code);
    }

    @Transactional
    public String saveInformation(User user, SignUpInfoDto signUpInfoDto) {

        List<Long> ottIdList = signUpInfoDto.getOttList();
        Long firstGenreId = signUpInfoDto.getFirstGenre();
        List<Long> genreIdList = signUpInfoDto.getGenreList();

        ottIdList.stream().forEach(id -> {
            UserSubscribingOTT userSubscribingOTT = UserSubscribingOTT.create(
                    user,
                    ottRepository.findById(id)
                            .orElseThrow(() -> new NotFoundException(ENTITY_NOT_FOUND))
            );
            userSubscribingOTTRepository.save(userSubscribingOTT);
        });

        UserGenre firstUserGenre = UserGenre.create(
                user,
                genreRepository.findById(firstGenreId)
                        .orElseThrow(() -> new NotFoundException(ENTITY_NOT_FOUND)),
                true
        );
        userGenreRepository.save(firstUserGenre);


        if(!genreIdList.isEmpty()) {
            genreIdList.stream().forEach(id -> {
                UserGenre userGenre = UserGenre.create(
                        user,
                        genreRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException(ENTITY_NOT_FOUND)),
                        false
                );
                userGenreRepository.save(userGenre);
            });
        }

        user.changeUserRole();
        return "추가정보 저장 완료";
    }

    @Transactional
    public void logout(User user, String accessToken) {
        SocialType socialType = user.getSocialType();
        if(user.getCode() == null) throw new UnauthorizedException(SIGNIN_EXPIRED);
        if(socialType.equals(SocialType.NAVER)) {
            unlinkNaver(user.getCode());
        }
        else {
            unlinkGoogle(user.getCode());
        }

        tokenService.removeByEmail(user.getEmail());
        user.changeCode(null);
        userRepository.save(user);
        tokenService.saveTokenInfo("expired", accessToken);
    }

    @Transactional
    public void withdrawal(User user, String accessToken){
        SocialType socialType = user.getSocialType();
        if(user.getCode() == null) throw new UnauthorizedException(SIGNIN_EXPIRED);
        if(socialType.equals(SocialType.NAVER)) {
            unlinkNaver(user.getCode());
        }
        else {
            unlinkGoogle(user.getCode());
        }
        tokenService.removeByEmail(user.getEmail());
        tokenService.saveTokenInfo("expired", accessToken);
        deleteUser(user);
    }

    private void unlinkNaver(String code) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://nid.naver.com/oauth2.0/token")
                .queryParam("client_id", naverClientId)
                .queryParam("client_secret", naverClientSecret)
                .queryParam("access_token", code)
                .queryParam("grant_type", "delete")
                .queryParam("service_provider", "NAVER")
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        try{
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            log.info(response.getBody());
        }catch (Exception e) {
            throw new UnauthorizedException(SIGNIN_EXPIRED);
        }

    }

    private void unlinkGoogle(String code) {
        String url = "https://oauth2.googleapis.com/revoke";

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", code);
        try{
            String response = restTemplate.postForObject(url, params, String.class);
            log.info(response);
        }
        catch (Exception e){
            throw new UnauthorizedException(SIGNIN_EXPIRED);
        }
    }

    private void deleteUser(User user) {
        userRepository.delete(user);
    }


}
