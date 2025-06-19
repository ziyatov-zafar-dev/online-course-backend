package uz.zafar.onlinecourse.dto.review_dto.res;

import lombok.*;
import uz.zafar.onlinecourse.dto.lesson_dto.res.LessonDto;
import uz.zafar.onlinecourse.dto.student_dto.res.StudentDto;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonReviewDto {
    private LessonDto lesson;
    private List<StudentDto> students;
    private long reviewCount;
}
