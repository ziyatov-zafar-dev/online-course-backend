package uz.zafar.onlinecourse.dto.homework_submission_file_dto.res;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HomeworkSubmissionFileDto {
    private UUID pkey;
    private String fileUrl;
    private Date created;
    private Short typeId;
    private String fileName;
    private UUID homeworkSubmissionId;
}
