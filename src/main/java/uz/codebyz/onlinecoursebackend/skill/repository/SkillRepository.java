package uz.codebyz.onlinecoursebackend.skill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.codebyz.onlinecoursebackend.skill.entity.Skill;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SkillRepository extends JpaRepository<Skill, UUID> {
    @Query("select s from Skill s where (s.active=true and s.id=:id)")
    Optional<Skill> findById(@Param("id") UUID id);


    @Query("select s from Skill s where (s.active=true and s.course.id=:courseId) order by s.orderNumber asc")
    List<Skill> getAllSkillsByCourse(@Param("courseId") UUID courseId);

}
