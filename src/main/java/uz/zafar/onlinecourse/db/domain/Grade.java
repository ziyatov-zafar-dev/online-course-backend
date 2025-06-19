package uz.zafar.onlinecourse.db.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import uz.zafar.onlinecourse.helper.TimeUtil;

import java.util.Date;
import java.util.UUID;

@Table(name = "grades")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Grade {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    private Long studentId;
    private Integer ball;
    private Double grade;
    private UUID lessonId;
    private Date createdAt = TimeUtil.currentTashkentTime();
    private Date updatedAt = TimeUtil.currentTashkentTime();
    private String comment;
}
