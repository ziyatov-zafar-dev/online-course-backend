package uz.zafar.onlinecourse.dto.comment_dto.req;

import lombok.*;

import java.util.UUID;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddCommentDto {
    private UUID lessonId;
    private Long studentId;
    private String comment;
}
