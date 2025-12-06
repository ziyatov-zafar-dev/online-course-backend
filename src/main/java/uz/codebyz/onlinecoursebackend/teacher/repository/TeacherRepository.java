package uz.codebyz.onlinecoursebackend.teacher.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.codebyz.onlinecoursebackend.teacher.entity.Teacher;
import uz.codebyz.onlinecoursebackend.teacher.entity.TeacherStatus;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @Query("select t from Teacher t where t.id=:id")
    boolean existsTeacherById(@Param("id") Long teacherId);

    @Query("select t from Teacher t order by t.id")
    List<Teacher> adminFindAll();

    @Query("select t from Teacher t order by t.id")
    Page<Teacher> adminFindAll(Pageable pageable);

    @Query("select t from Teacher t where t.status=:status order by t.id")
    List<Teacher> getAllTeachersByStatus(@Param("status") TeacherStatus status);

    @Query("select t from Teacher t where t.status=:status order by t.id")
    Page<Teacher> getAllTeachersByStatus(@Param("status") TeacherStatus status, Pageable pageable);
}
