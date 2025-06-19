package uz.zafar.onlinecourse.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginResponseDto {
   private String access_token;
   private String refresh_token;
   private Long expireDate;
}
