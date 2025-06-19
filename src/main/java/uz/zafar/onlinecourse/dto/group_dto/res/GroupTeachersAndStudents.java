package uz.zafar.onlinecourse.dto.group_dto.res;

import lombok.*;
import uz.zafar.onlinecourse.dto.student_dto.res.StudentDto;
import uz.zafar.onlinecourse.dto.teacher_dto.res.TeacherDto;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupTeachersAndStudents {
    private List<TeacherDto> teachers;
    private List<StudentDto> students;
    private GroupDto group;
}
