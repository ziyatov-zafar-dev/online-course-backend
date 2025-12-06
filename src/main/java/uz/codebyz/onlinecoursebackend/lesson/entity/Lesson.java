package uz.codebyz.onlinecoursebackend.lesson.entity;

import jakarta.persistence.*;
import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.homework.entity.Homework;
import uz.codebyz.onlinecoursebackend.module.entity.Module;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Boolean active;
    private Boolean deleted;
    @Column(unique = true, nullable = false)
    private String slug;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id")
    private Module module;
    private Integer orderNumber;
    @Column(columnDefinition = "TEXT")
    private String videoUrl;
    private String videName;
    private Long videoDurationSize;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Boolean hasHomework;
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Homework> homework = new ArrayList<>();

}
