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
public class AddHomeworkFileDto {
    private UUID homeworkId;
    private MultipartFile file;
    private Short typeId;
}
