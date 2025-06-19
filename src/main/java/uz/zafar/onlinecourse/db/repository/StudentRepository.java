package uz.zafar.onlinecourse.db.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.zafar.onlinecourse.db.domain.Student;
import uz.zafar.onlinecourse.dto.student_dto.res.StudentDto;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("""
            select
            s.id as studentId,
            s.user.id as userId,
            s.user.username as username,
            s.user.firstname as firstname,
            s.user.lastname as lastname,
            s.user.email as email
            from Student s where s.user.active=true""")
    Page<StudentDto> findAllStudents(Pageable pageable);

    @Query("""
            select  s.id as studentId,
                s.user.id as userId,
                s.user.username as username,
                s.user.firstname as firstname,
                s.user.lastname as lastname,
                s.user.email as email
            from Student s
            where s.id=:s_id and s.user.active=true
            """)
    StudentDto findByStudentId(@Param("s_id") Long studentId);

}
