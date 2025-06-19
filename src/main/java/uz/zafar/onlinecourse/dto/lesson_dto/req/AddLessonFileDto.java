package uz.zafar.onlinecourse.dto.lesson_dto.req;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddLessonFileDto {
    private UUID lessonId;
    private MultipartFile file;
    private Short typeId;
}
