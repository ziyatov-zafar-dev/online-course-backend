package uz.zafar.onlinecourse.dto.lesson_dto.res;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeDto {
    private long likeCount;
    private boolean liked;
}
