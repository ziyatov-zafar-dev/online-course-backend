package uz.zafar.onlinecourse.service;

import jakarta.servlet.http.HttpServletRequest;
import uz.zafar.onlinecourse.db.domain.HomeworkSubmission;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.homework_submission_dto.req.AddHomeworkSubmissionDto;
import uz.zafar.onlinecourse.dto.homework_submission_dto.res.SubmissionDto;

import java.util.UUID;

public interface HomeworkSubmissionService {
    ResponseDto<HomeworkSubmission> addHomeworkSubmission(AddHomeworkSubmissionDto homeworkSubmission, HttpServletRequest r) throws Exception;

    ResponseDto<SubmissionDto> getHomeworkSubmissionByHomeworkIdAndStudentId(UUID homeworkId, Long studentId) throws Exception;
    ResponseDto<Void>removeHomeworkSubmissionByHomeworkId(UUID homeworkId, Long studentId) throws Exception;

}
