package uz.zafar.onlinecourse.dto.lesson_dto.res;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonFileDto {
    private MultipartFile file;
    private Short typeId;
}
