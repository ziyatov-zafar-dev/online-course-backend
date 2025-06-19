package uz.zafar.onlinecourse.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.homework_dto.req.AddHomeworkDto;
import uz.zafar.onlinecourse.dto.homework_dto.req.AddHomeworkFileDto;
import uz.zafar.onlinecourse.dto.homework_dto.req.EditHomeworkDto;
import uz.zafar.onlinecourse.dto.homework_dto.req.RemoveHomeworkFile;
import uz.zafar.onlinecourse.dto.homework_dto.res.HomeworkDto;

import java.util.UUID;

public interface HomeworkService {
    ResponseDto<HomeworkDto> findById(UUID pkey);

    ResponseDto<Page<HomeworkDto>> findAllByLessonId(UUID lessonId, int page, int size);

    ResponseDto<?> addHomework(AddHomeworkDto homework, HttpServletRequest req);

    ResponseDto<HomeworkDto> edit(UUID homeworkId, EditHomeworkDto homework);

    ResponseDto<HomeworkDto> removeFile(RemoveHomeworkFile remove);

    ResponseDto<?> addFileToHomework(AddHomeworkFileDto file, HttpServletRequest req);

    ResponseEntity<?> downloadLessonHomeworks(UUID lessonId);
}
