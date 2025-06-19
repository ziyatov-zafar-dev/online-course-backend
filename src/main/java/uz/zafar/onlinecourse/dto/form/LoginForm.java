package uz.zafar.onlinecourse.dto.form;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoginForm {
  private String username;
  private String password;
}
