package uz.zafar.onlinecourse.db.repository;

//import com.example.onlinecoursemain.db.domain.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.zafar.onlinecourse.db.domain.Teacher;
import uz.zafar.onlinecourse.dto.teacher_dto.res.TeacherDto;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @Query("""
            select
            t.id as teacherId,
            t.user.id as userId,
            t.user.username as username,
            t.user.firstname as firstname,
            t.user.lastname as lastname,
            t.user.email as email
            from Teacher t where t.user.active=true""")
    Page<TeacherDto> getAllTeachers(Pageable pageable);
    @Query("""
            select  t.id as teacherId,
                t.user.id as userId,
                t.user.username as username,
                t.user.firstname as firstname,
                t.user.lastname as lastname,
                t.user.email as email
            from Teacher t
            where t.id=:t_id and t.user.active=true
            """)
    TeacherDto findTeacherById(@Param("t_id") Long teacherId);
}
