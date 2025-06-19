package uz.zafar.onlinecourse.dto.lesson_dto.req;

import lombok.*;
import uz.zafar.onlinecourse.dto.lesson_dto.res.LessonFileDto;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddLessonDto {
    private String title;
    private String description;
    private UUID groupId;
    private List<LessonFileDto> files;
}
