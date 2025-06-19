package uz.zafar.onlinecourse.dto.group_dto.req;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditGroupDto {
    private String name;
    private String description;
    private String telegramChannel;
    private Boolean hasTelegramChannel;
}
