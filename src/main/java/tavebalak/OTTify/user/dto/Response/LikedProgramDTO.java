package tavebalak.OTTify.user.dto.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikedProgramDTO {
    private Long programId;
    private String posterPath;

    @Builder
    public LikedProgramDTO(Long programId, String posterPath) {
        this.programId = programId;
        this.posterPath = posterPath;
    }
}
