package uz.zafar.onlinecourse.dto.lesson_file_dto.res;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DownloadLessonFileDto {
    private String fileName;
    private String fileUrl;
}
