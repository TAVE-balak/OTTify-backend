package tavebalak.OTTify.user.dto.Response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class UninterestedProgramListDTO {
    private int totalCnt;
    private List<UninterestedProgramDTO> uninterestedProgramList;

    @Builder
    public UninterestedProgramListDTO(int totalCnt, List<UninterestedProgramDTO> uninterestedProgramList) {
        this.totalCnt = totalCnt;
        this.uninterestedProgramList = uninterestedProgramList;
    }
}
