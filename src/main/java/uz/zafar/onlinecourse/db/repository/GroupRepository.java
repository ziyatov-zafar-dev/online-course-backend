package uz.zafar.onlinecourse.db.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.zafar.onlinecourse.db.domain.*;
import uz.zafar.onlinecourse.dto.date.DateDto;
import uz.zafar.onlinecourse.dto.group_dto.res.GroupDto;
import uz.zafar.onlinecourse.dto.group_dto.res.GroupInformationDto;
import uz.zafar.onlinecourse.dto.student_dto.res.StudentDto;
import uz.zafar.onlinecourse.dto.teacher_dto.res.TeacherDto;

import java.util.List;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, UUID> {
    @Query(nativeQuery = true, value = """
            select
                    t.id as teacherId,u.id as userId, u.username,
                    u.lastname, u.firstname,u.email
            from users u
            inner join teachers t
                        on t.id=u.teacher_id
            inner join teacher_groups tg
                        on tg.teacher_id=t.id
            inner join groups g
                        on g.id=tg.group_id
            where g.id=:groupId and g.active=true""")
    List<TeacherDto> getAllByTeachersOfGroup(UUID groupId);

    @Query("""
                SELECT g FROM Group g
                WHERE g.course.id = :courseId and g.active=true
                order by g.created
            """)
    Page<Group> getAllGroupsByCourseId(@Param("courseId") UUID courseId, Pageable pageable);

    @Query("""
                SELECT g FROM Group g
                WHERE g.active=true
                order by g.created
            """)
    Page<Group> findAll(Pageable pageable);

    @Query("""
            select
            round(extract(year from g.updated)) as year ,
            round(extract(month from g.updated)) as month ,
            round(extract(day from g.updated)) as day ,
            round(extract(minute from g.updated)) as minute ,
            round(extract(hour from g.updated)) as hour ,
            round(extract(second from g.updated)) as second
            from Group g where g.id=:pkey and g.active=true""")
    DateDto getGroupOfUpdatedDate(@Param("pkey") UUID pkey);

    @Query("""
            select
            round(extract(year from g.created)) as year ,
            round(extract(month from g.created)) as month ,
            round(extract(day from g.created)) as day ,
            round(extract(minute from g.created)) as minute ,
            round(extract(hour from g.created)) as hour ,
            round(extract(second from g.created)) as second
            from Group g where g.id=:pkey and g.active=true""")
    DateDto getGroupOfCreatedDate(@Param("pkey") UUID pkey);

    @Query(value = """
                        select s.id as studentId,
                               u.id as userId,
                               u.username,u.firstname,
                               u.lastname,u.email
                        from students s
                        inner join users u
                            on u.student_id=s.id
                        inner join student_groups sg
                            on sg.student_id=s.id
                        inner join groups g
                            on g.id=sg.group_id
                        where g.id=:group_id and g.active=true
                        order by 1;
            """, nativeQuery = true)
    List<StudentDto> getAllByStudentsOfGroup(@Param("group_id") UUID groupId);

    @Query(nativeQuery = true, value = """
            SELECT g.*
            FROM groups g
            INNER JOIN teacher_groups tg ON tg.group_id = g.id
            INNER JOIN teachers t ON t.id = tg.teacher_id
            INNER JOIN users u ON u.teacher_id = t.id
            WHERE t.id = :teacherId
            """)
    Page<Group> findAllByTeacherId(@Param("teacherId") Long teacherId, Pageable pageable);

    @Query(nativeQuery = true, value = """
            SELECT g.*       
            FROM groups g
            INNER JOIN student_groups sg ON sg.group_id = g.id
            INNER JOIN students s ON s.id = sg.student_id
            INNER JOIN users u ON u.student_id = t.id
            WHERE t.id = :studentId
            """)
    Page<Group> findAllByStudentId(@Param("studentId") Long studentId, Pageable pageable);

    @Query(value = """
            select s.id as studentId,
                   u.id as userId,
                   u.username,u.firstname,
                   u.lastname,u.email
            from students s
            inner join student_groups sg on sg.student_id=s.id
            inner join groups g on g.id=sg.group_id
            inner join users u on u.student_id=s.id
            """, nativeQuery = true)
    List<StudentDto> getGroupStudents();

    @Query("""
                                    select g
                        from StudentGroup sg
                        inner join Student s on s.id=sg.studentId
                        inner join Group g on g.id = sg.groupId
                        where sg.studentId=:studentId and sg.groupId=:groupId and sg.active=true
            """)
    List<Group> isJoinGroup(@Param("groupId") UUID groupId, @Param("studentId") Long studentId);

    @Query("""
            WITH created AS (
                SELECT g.created AS created1
                FROM Group g
                WHERE g.id = :groupId
            ), 
            updated AS (
                SELECT g.updated AS updated
                FROM Group g
                WHERE g.id = :groupId
            ), 
            studentCount AS (
                SELECT COUNT(s) AS student_count
                FROM Student s
                INNER JOIN StudentGroup sg ON sg.studentId = s.id
                INNER JOIN Group g ON g.id = sg.groupId
                WHERE g.id = :groupId and sg.active=true
            ), 
            lessonCount AS (
                SELECT COUNT(l) AS lesson_count
                FROM Lesson l
                WHERE l.group.id = :groupId
            )
            
            SELECT 
                c.created1 AS createdGroup,
                u.updated AS lastEdited,
                sc.student_count AS studentCount,
                lc.lesson_count AS lessonCount
            FROM created c, updated u, studentCount sc, lessonCount lc
            """)
    GroupInformationDto information(@Param("groupId") UUID groupId);

    @Query("""
            select  g
            from Group g
            inner join StudentGroup sg on sg.groupId=g.id
            inner join Student s on s.id=sg.studentId
            where sg.studentId=:studentId and sg.active=true
            order by sg.created asc
            """)
    List<Group> getGroupsStudent(@Param("studentId") Long studentId);
}
