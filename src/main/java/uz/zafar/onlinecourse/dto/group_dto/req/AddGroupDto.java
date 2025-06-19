package uz.zafar.onlinecourse.dto.group_dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddGroupDto {
    private String name;
    private String description;
    private String telegramChannel;
    private Boolean hasTelegramChannel;
    @JsonProperty("course-pkey")
    private UUID coursePkey;
    private Long teacherId;
}
