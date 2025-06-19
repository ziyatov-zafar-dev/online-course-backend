package uz.zafar.onlinecourse.dto.review_dto.req;

import lombok.*;

import java.util.UUID;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddReviewDto {
    private Long studentId;
    private UUID lessonId;
}
