package tavebalak.OTTify.user.dto.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UninterestedProgramDTO {
    private Long programId;
    private String posterPath;

    @Builder
    public UninterestedProgramDTO(Long programId, String posterPath) {
        this.programId = programId;
        this.posterPath = posterPath;
    }
}
