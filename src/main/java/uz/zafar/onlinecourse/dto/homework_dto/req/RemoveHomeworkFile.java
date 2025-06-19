package uz.zafar.onlinecourse.dto.homework_dto.req;

import lombok.*;

import java.util.UUID;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RemoveHomeworkFile {
    private UUID homeworkId;
    private UUID homeworkFileId;
}
