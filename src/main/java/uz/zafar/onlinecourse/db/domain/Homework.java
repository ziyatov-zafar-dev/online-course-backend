package uz.zafar.onlinecourse.db.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import uz.zafar.onlinecourse.helper.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Table(name = "homeworks")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Homework {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    private String title;
    private String description;
    private Date deadline;
    @ManyToOne(fetch = FetchType.EAGER)
    private Lesson lesson;
    private Date created = TimeUtil.currentTashkentTime();
    private Date updated = TimeUtil.currentTashkentTime();
    private Boolean active = true;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "homework", cascade = CascadeType.ALL)
    private List<HomeworkFile> files = new ArrayList<>();
/*
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "homework", cascade = CascadeType.ALL)
    private List<HomeworkSubmission> submissions = new ArrayList<>();
*/

}
