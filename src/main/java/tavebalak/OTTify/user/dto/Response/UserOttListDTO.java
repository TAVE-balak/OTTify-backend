package tavebalak.OTTify.user.dto.Response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class UserOttListDTO {
    private int totalCnt;
    private List<UserOttDTO> ottList;

    @Builder
    public UserOttListDTO(int totalCnt, List<UserOttDTO> ottList) {
        this.totalCnt = totalCnt;
        this.ottList = ottList;
    }
}
