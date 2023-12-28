package tavebalak.OTTify.community.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReplyListsDTO {
    private Long recommentId;
    private String content;
    private String nickName;
    private Long userId;
    private LocalDateTime createdAt;

    @Builder
    public ReplyListsDTO(Long recommentId, String content, String nickName, Long userId, LocalDateTime createdAt) {
        this.recommentId = recommentId;
        this.content = content;
        this.nickName = nickName;
        this.userId = userId;
        this.createdAt = createdAt;
    }
}
