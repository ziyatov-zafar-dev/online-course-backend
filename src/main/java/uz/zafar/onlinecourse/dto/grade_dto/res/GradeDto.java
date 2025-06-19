package uz.zafar.onlinecourse.dto.grade_dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;
import java.util.UUID;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeDto {
    @JsonProperty("grade-id")
    private UUID id;
    private Double grade;
    private Integer ball;
    private UUID lessonId;
    private Long studentId;
    private Date createdAt;
    private Date updatedAt;
    @Column(columnDefinition = "TEXT")
    private String comment;
}
