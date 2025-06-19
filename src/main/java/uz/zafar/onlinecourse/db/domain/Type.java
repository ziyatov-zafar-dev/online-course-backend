package uz.zafar.onlinecourse.db.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "types")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;
    @Column(columnDefinition = "text",nullable = false,unique = true)
    @JsonProperty("type")
    private String name;
    @JsonIgnore
    private Boolean active = true;
    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "type",cascade = CascadeType.ALL)
    private List<LessonFile> lessonFiles;
    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "type",cascade = CascadeType.ALL)
    private List<HomeworkFile> homeworkFiles;
}
