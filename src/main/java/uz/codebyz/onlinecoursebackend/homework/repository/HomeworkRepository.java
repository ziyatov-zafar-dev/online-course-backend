package uz.codebyz.onlinecoursebackend.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.codebyz.onlinecoursebackend.homework.entity.Homework;

import java.util.Optional;
import java.util.UUID;

public interface HomeworkRepository extends JpaRepository<Homework, UUID> {
    @Query("select h from Homework h where (h.active=true  and h.id=:id)")
    Optional<Homework> getHomeworkById(@Param("id") UUID id);
}
