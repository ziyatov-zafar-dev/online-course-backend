package uz.zafar.onlinecourse.dto.group_dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.zafar.onlinecourse.dto.course_dto.res.CourseDto;
import uz.zafar.onlinecourse.dto.date.DateDto;
import uz.zafar.onlinecourse.dto.lesson_dto.res.LessonDto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupLessonsAndGroupDto {
    private List<LessonDto> getLessons;
    private GroupDto group;
}
