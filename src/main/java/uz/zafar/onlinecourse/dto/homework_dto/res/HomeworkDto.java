package uz.zafar.onlinecourse.dto.homework_dto.res;

import lombok.*;
import uz.zafar.onlinecourse.dto.lesson_dto.res.LessonDto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HomeworkDto {
    private UUID pkey;
    private String title;
    private String description;
    private Date deadline;
    private Date updated;
    private Date created;
    private List<HomeworkFileDto> files;
    private LessonDto lesson;
}
