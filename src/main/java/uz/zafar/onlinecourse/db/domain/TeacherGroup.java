package uz.zafar.onlinecourse.db.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import uz.zafar.onlinecourse.helper.TimeUtil;

import java.util.Date;
import java.util.UUID;

@Table(name = "teacher_groups")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherGroup {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    private Long teacherId;
    private UUID groupId;
    private Date created = TimeUtil.currentTashkentTime();
}
