package tavebalak.OTTify.user.dto.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tavebalak.OTTify.user.entity.UserSubscribingOTT;

@Getter
@NoArgsConstructor
public class UserOttDTO {
    private Long id;
    private String name;
    private String logoPath;

    @Builder
    public UserOttDTO(Long id, String name, String logoPath) {
        this.id = id;
        this.name = name;
        this.logoPath = logoPath;
    }

    public UserOttDTO(UserSubscribingOTT uso) {
        this.id = uso.getOtt().getId();
        this.name = uso.getOtt().getName();
        this.logoPath = uso.getOtt().getLogoPath();
    }
}
