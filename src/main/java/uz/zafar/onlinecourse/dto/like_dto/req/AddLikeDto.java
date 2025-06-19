package uz.zafar.onlinecourse.dto.like_dto.req;

import lombok.*;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddLikeDto {
    private Long studentId;
    private UUID lessonId;
}
