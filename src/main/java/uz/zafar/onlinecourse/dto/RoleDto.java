package uz.zafar.onlinecourse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class RoleDto {
    @JsonProperty("pkey")
    private Long id;
    @JsonProperty(value = "role-name",required = true)
    private String roleName;
}
