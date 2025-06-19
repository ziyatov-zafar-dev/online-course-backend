package uz.zafar.onlinecourse.dto.review_dto.res;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDto {
    private UUID id;
    private Date reviewDate;
    private Long studentId;
    private UUID lessonId;
}
