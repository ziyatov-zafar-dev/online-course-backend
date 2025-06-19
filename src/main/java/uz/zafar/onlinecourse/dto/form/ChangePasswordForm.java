package uz.zafar.onlinecourse.dto.form;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ChangePasswordForm {
    private String oldPassword;
    private String newPassword;
}
