package uz.codebyz.onlinecoursebackend.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.codebyz.onlinecoursebackend.student.entity.Student;

import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {

}
