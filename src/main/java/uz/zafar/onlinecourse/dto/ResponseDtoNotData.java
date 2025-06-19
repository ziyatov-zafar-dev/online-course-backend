package uz.zafar.onlinecourse.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResponseDtoNotData {
    private boolean success;
    private String message;
}
