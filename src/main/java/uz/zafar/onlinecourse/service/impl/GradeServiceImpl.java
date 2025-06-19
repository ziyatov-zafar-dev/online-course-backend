package uz.zafar.onlinecourse.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import uz.zafar.onlinecourse.db.domain.Grade;
import uz.zafar.onlinecourse.db.domain.Lesson;
import uz.zafar.onlinecourse.db.domain.Student;
import uz.zafar.onlinecourse.db.repository.GradeRepository;
import uz.zafar.onlinecourse.db.repository.LessonRepository;
import uz.zafar.onlinecourse.db.repository.StudentRepository;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.grade_dto.req.AddGradeDto;
import uz.zafar.onlinecourse.dto.grade_dto.req.EditGradeDto;
import uz.zafar.onlinecourse.dto.grade_dto.res.GradeDto;
import uz.zafar.onlinecourse.helper.TimeUtil;
import uz.zafar.onlinecourse.service.GradeService;

import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;

    @Override
    public ResponseDto<GradeDto> getLessonIdAndStudentIdGrade(UUID lessonId, Long studentId) {
        try {
            Optional<Grade> gOp = gradeRepository.findByStudentIdAndLessonId(studentId, lessonId);
            if (gOp.isEmpty())
                throw new Exception("not exists student grade this lesson");
            Grade grade = gOp.get();
            return new ResponseDto<>(true, "success", GradeDto.builder()
                    .id(grade.getId())
                    .grade(grade.getGrade())
                    .ball(grade.getBall())
                    .lessonId(grade.getLessonId())
                    .studentId(grade.getStudentId())
                    .createdAt(grade.getCreatedAt())
                    .updatedAt(grade.getUpdatedAt())
                    .comment(grade.getComment())
                    .build());
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<GradeDto> evaluate(AddGradeDto dto) {
        try {
            Optional<Grade> gOp = gradeRepository.findByStudentIdAndLessonId(dto.getStudentId(), dto.getLessonId());
            if (gOp.isPresent())
                throw new Exception("You have already rated this student.");
            Grade grade = new Grade();
            Optional<Student> sOp = studentRepository.findById(dto.getStudentId());
            if (sOp.isEmpty())
                throw new Exception("not exists student student");
            Student student = sOp.get();
            Optional<Lesson> lOp = lessonRepository.findById(dto.getLessonId());
            if (lOp.isEmpty())
                throw new Exception("not exists lesson");
            Lesson lesson = lOp.get();

            grade.setStudentId(student.getId());
            grade.setLessonId(lesson.getId());
            grade.setBall(dto.getBall());
            grade.setGrade(dto.getGrade());
            grade.setComment(dto.getComment());
            grade.setCreatedAt(TimeUtil.currentTashkentTime());
            grade.setUpdatedAt(TimeUtil.currentTashkentTime());
            grade = gradeRepository.save(grade);
            return new ResponseDto<>(true, "success", GradeDto.builder()
                    .id(grade.getId())
                    .grade(grade.getGrade())
                    .ball(grade.getBall())
                    .lessonId(grade.getLessonId())
                    .studentId(grade.getStudentId())
                    .createdAt(grade.getCreatedAt())
                    .updatedAt(grade.getUpdatedAt())
                    .comment(grade.getComment())
                    .build());
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<GradeDto> editGrade(EditGradeDto dto, UUID gradeId) {
        try {
            Long studentId = dto.getStudentId();
            UUID lessonId = dto.getLessonId();
            Optional<Student> sOp = studentRepository.findById(studentId);
            if (sOp.isEmpty())
                throw new Exception("not exists student student");

            Optional<Lesson> lOp = lessonRepository.findById(lessonId);
            if (lOp.isEmpty())
                throw new Exception("not exists lesson");

            Optional<Grade> gOp = gradeRepository.findByStudentIdAndLessonId(studentId, lessonId);
            if (gOp.isEmpty())
                throw new Exception("You haven't rated this student.");
            gOp = gradeRepository.findById(gradeId);
            if (gOp.isEmpty())
                throw new Exception("Not found grade.");
            Grade grade = gOp.get();
            grade.setStudentId(studentId);
            grade.setLessonId(lessonId);
            grade.setBall(dto.getBall());
            grade.setGrade(dto.getGrade());
            grade.setComment(dto.getComment());
            grade.setUpdatedAt(TimeUtil.currentTashkentTime());
            grade = gradeRepository.save(grade);
            return new ResponseDto<>(true, "success", GradeDto.builder()
                    .id(grade.getId())
                    .grade(grade.getGrade())
                    .ball(grade.getBall())
                    .lessonId(grade.getLessonId())
                    .studentId(grade.getStudentId())
                    .createdAt(grade.getCreatedAt())
                    .updatedAt(grade.getUpdatedAt())
                    .comment(grade.getComment())
                    .build());
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }
}
