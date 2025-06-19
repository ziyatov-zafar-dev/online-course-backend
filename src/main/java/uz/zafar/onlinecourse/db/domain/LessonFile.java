package uz.zafar.onlinecourse.db.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import uz.zafar.onlinecourse.helper.TimeUtil;

import java.util.Date;
import java.util.UUID;

@Table(name = "lesson-files")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonFile {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    private String fileUrl;
    private String fileName;
    private Date created = TimeUtil.currentTashkentTime();
    @ManyToOne(fetch = FetchType.EAGER)
    private Type type;
    @ManyToOne(fetch = FetchType.EAGER)
    private Lesson lesson;
    private Boolean active = true;
}
