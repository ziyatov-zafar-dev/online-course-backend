package uz.codebyz.onlinecoursebackend.certificate.entity;

import jakarta.persistence.*;
import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.student.entity.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "certificates")
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;
    // 🔗 Student bilan bog‘lash
    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id")
    private Course course;
    @OneToMany(
            mappedBy = "certificate",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CertificateFile> files = new ArrayList<>();
}


