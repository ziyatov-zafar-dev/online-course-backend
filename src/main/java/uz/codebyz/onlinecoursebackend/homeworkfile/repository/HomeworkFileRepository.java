package uz.codebyz.onlinecoursebackend.homeworkfile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.codebyz.onlinecoursebackend.homework.entity.Homework;
import uz.codebyz.onlinecoursebackend.homeworkfile.entity.HomeworkFile;

import java.util.UUID;

public interface HomeworkFileRepository extends JpaRepository<HomeworkFile, UUID> {
}
