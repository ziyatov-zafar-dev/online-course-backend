package uz.zafar.onlinecourse.dto.group_dto.res;

import lombok.*;
import uz.zafar.onlinecourse.dto.course_dto.res.CourseDto;
import uz.zafar.onlinecourse.dto.date.DateDto;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupDto {
    private UUID pkey;
    private String name;
    private String description;
    private String telegramChannel;
    private Boolean hasTelegramChannel;
    private CourseDto course;
    private DateDto created;
    private DateDto updated;
}
