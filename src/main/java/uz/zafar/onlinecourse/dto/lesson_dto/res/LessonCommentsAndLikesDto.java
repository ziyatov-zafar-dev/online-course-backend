package uz.zafar.onlinecourse.dto.lesson_dto.res;

import lombok.*;
import uz.zafar.onlinecourse.dto.comment_dto.res.CommentDto;

import java.util.List;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonCommentsAndLikesDto {
    private LessonDto lesson;
    private List<CommentDto> comments;
    private LikeDto likes;
}
