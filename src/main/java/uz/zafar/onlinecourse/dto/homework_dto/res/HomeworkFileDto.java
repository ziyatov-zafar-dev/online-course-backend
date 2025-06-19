package uz.zafar.onlinecourse.dto.homework_dto.res;

import lombok.*;

import java.util.UUID;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HomeworkFileDto {
    private UUID homeworkFileId;
    private String fileUrl;
    private String fileName;
    private Short typeId;
    private UUID homeworkId;
}
