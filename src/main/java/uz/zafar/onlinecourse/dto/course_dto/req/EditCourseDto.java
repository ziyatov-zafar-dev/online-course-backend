package uz.zafar.onlinecourse.dto.course_dto.req;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditCourseDto {
    private String name;
    private String description;
    private String telegramChannel;
    private Boolean hasTelegramChannel;
}
