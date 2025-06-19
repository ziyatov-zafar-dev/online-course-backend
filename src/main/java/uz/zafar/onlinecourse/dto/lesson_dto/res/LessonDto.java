package uz.zafar.onlinecourse.dto.lesson_dto.res;

import lombok.*;
import uz.zafar.onlinecourse.dto.date.DateDto;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonDto {
    private UUID pkey;
    private String title;
    private String description;
    private DateDto created;
    private DateDto updated;
    private UUID groupId;
    private List<LessonFileAndTypeDto> files;
}
