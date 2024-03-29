package tavebalak.OTTify.community.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReplyCommentEditorDTO {
    private String comment;

    public ReplyCommentEditorDTO(String comment){
        this.comment = comment;
    }

    public ReplyCommentEditorDTO changeComment(String comment){
        this.comment = comment;
        return this;
    }

}
