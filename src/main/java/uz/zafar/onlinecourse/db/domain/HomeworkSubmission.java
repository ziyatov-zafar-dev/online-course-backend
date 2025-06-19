package uz.zafar.onlinecourse.db.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@Table(name = "homework-submissions")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HomeworkSubmission {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    private Long studentId;
    private UUID lessonId;
    private UUID homeworkId;
/*
    @ManyToOne(fetch = FetchType.EAGER)
    private Homework homework;
*/
    @JsonIgnore
    private Boolean active;
    private Date created;
/*
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "homeworkSubmission", cascade = CascadeType.ALL)
    private List<HomeworkSubmissionFile> files;
*/

}
