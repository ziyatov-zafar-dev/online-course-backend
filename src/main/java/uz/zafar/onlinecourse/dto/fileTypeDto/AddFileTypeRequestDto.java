package uz.zafar.onlinecourse.dto.fileTypeDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddFileTypeRequestDto {
    @JsonProperty("type")
    private String fileType;
}
