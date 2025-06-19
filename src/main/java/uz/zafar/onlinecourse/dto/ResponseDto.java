package uz.zafar.onlinecourse.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResponseDto<E> {
    private boolean success;
    private String message;
    private E data;
    public ResponseDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
