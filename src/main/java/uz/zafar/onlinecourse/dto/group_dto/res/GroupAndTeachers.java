package uz.zafar.onlinecourse.dto.group_dto.res;

import lombok.*;
import uz.zafar.onlinecourse.dto.teacher_dto.res.TeacherDto;

import java.util.List;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupAndTeachers {
    private List<TeacherDto>teachers;
    private GroupDto group;
}
