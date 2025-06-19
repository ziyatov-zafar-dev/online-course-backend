package uz.zafar.onlinecourse.db.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import uz.zafar.onlinecourse.helper.TimeUtil;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Table(name = "lessons")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lesson {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    private String title;
    private String description;
    private Date created = TimeUtil.currentTashkentTime();
    private Date updated = TimeUtil.currentTashkentTime();
    @ManyToOne(fetch = FetchType.EAGER)
    private Group group;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "lesson")
    private List<Homework> homeworks;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<LessonFile> files;
    private Boolean active = true;

}
