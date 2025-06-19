package uz.zafar.onlinecourse.dto.user_dto.req;

import lombok.*;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpForm {
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String email;
}
