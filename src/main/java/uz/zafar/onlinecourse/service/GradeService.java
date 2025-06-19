package uz.zafar.onlinecourse.service;

import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.grade_dto.req.AddGradeDto;
import uz.zafar.onlinecourse.dto.grade_dto.req.EditGradeDto;
import uz.zafar.onlinecourse.dto.grade_dto.res.GradeDto;

import java.util.UUID;

public interface GradeService {
    ResponseDto<GradeDto> getLessonIdAndStudentIdGrade(UUID lessonId, Long studentId);

    ResponseDto<GradeDto> evaluate(AddGradeDto dto);

    ResponseDto<GradeDto> editGrade(EditGradeDto dto, UUID gradeId);

}