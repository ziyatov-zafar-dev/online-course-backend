package uz.zafar.onlinecourse.dto.grade_dto.req;

import lombok.*;

import java.util.UUID;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddGradeDto {
    private Long studentId;
    private Integer ball;
    private Double grade;
    private UUID lessonId;
    private String comment;
}
