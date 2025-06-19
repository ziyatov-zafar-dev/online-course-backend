package uz.zafar.onlinecourse.dto.homework_dto.req;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditHomeworkDto {
    private String title;
    private String description;
    private Date deadline;
}
