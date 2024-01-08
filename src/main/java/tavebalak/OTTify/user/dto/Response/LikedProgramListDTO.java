package tavebalak.OTTify.user.dto.Response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class LikedProgramListDTO {
    private int totalCnt;
    private List<LikedProgramDTO> likedProgramList;

    @Builder
    public LikedProgramListDTO(int totalCnt, List<LikedProgramDTO> likedProgramList) {
        this.totalCnt = totalCnt;
        this.likedProgramList = likedProgramList;
    }
}
