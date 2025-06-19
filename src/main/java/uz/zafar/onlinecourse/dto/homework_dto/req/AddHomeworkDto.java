package uz.zafar.onlinecourse.dto.homework_dto.req;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddHomeworkDto {
    private EditHomeworkDto homework;
    private MultipartFile file;
    private Short typeId;
    private UUID lessonId;
}
