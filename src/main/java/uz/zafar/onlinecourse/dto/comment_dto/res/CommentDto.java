package uz.zafar.onlinecourse.dto.comment_dto.res;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Boolean me;
    private UUID commentId;
    private String comment;
    private UUID lessonId;
    private Long studentId;

    private Boolean edited;
    private Date createdAt;
    private Date lastEditedAt;

}
