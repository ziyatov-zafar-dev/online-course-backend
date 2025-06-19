package uz.zafar.onlinecourse.dto.homework_submission_dto.res;

import lombok.*;
import uz.zafar.onlinecourse.dto.homework_submission_file_dto.res.HomeworkSubmissionFileDto;

import java.util.UUID;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionDto {
    private UUID submissionId;
    private Long studentId;
    private UUID lessonId;
    private UUID homeworkId;
    private HomeworkSubmissionFileDto file;

}
