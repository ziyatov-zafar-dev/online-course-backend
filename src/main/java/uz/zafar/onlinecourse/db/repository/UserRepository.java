package uz.zafar.onlinecourse.db.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.zafar.onlinecourse.db.domain.Role;
import uz.zafar.onlinecourse.db.domain.User;
import uz.zafar.onlinecourse.dto.date.DateDto;
import uz.zafar.onlinecourse.dto.statistic_dto.res.StatisticDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
            select u from User u
            where u.username=:username and u.verified=true""")
    Optional<User> findByUsername(@Param("username") String username);

    @Query(value = """
            select r.*
            from roles r
            inner join user_role ur
                on ur.role_id=r.id
            inner join users u
               on u.id=ur.user_id
            where u.id=:user_id and u.is_verified=true
            order by r.id
            """, nativeQuery = true)
    List<Role> getRolesFromUser(@Param("user_id") Long userId);

    /*@Query("""
            select
            extract(year from u.created) as year,
            extract(month from u.created) as month,
            extract(day from u.created) as day,
            extract(hour from u.created) as hour,
            extract(minute from u.created) as minute,
            round(extract(second from u.created)) as second
            from User u where u.id=:user_id
            """)
    DateDto getUserCreatedDate(@Param("user_id") Long user_id);*/
    @Query("""
                select
                    extract(year from u.created),
                    extract(month from u.created),
                    extract(day from u.created),
                    extract(hour from u.created),
                    extract(minute from u.created),
                    round(extract(second from u.created))
                from User u
                where u.id = :user_id and u.verified=true
            """)
    DateDto getUserCreatedDate(@Param("user_id") Long userId);


    @Query(value = """
                select 
                    extract(year from u.updated) as year,
                    extract(month from u.updated) as month,
                    extract(day from u.updated) as day,
                    extract(hour from u.updated) as hour,
                    extract(minute from u.updated) as minute,
                    round(extract(second from u.updated)) as second
                from users u
                where u.id = :user_id and u.is_verified=true
            """, nativeQuery = true)
    DateDto getUserUpdatedDate(@Param("user_id") Long user_id);


    Page<User> findAllByActive(boolean active, Pageable pageable);

    Page<User> findAllByActiveOrderById(boolean active, Pageable pageable);

    Optional<User> findByEmail(String email);

    Optional<User> findByVerificationCode(String code);

    @Query(nativeQuery = true, value = """
            WITH userCount AS (
                  SELECT COUNT(*) AS userCount
                  FROM users u
                  WHERE u.active = true AND u.is_verified = true
              ), teacherCount AS (
                  SELECT COUNT(*) AS teacherCount
                  FROM teachers
              ), studentCount AS (
                  SELECT COUNT(*) AS studentCount
                  FROM students
              ), courseCount AS (
                  SELECT COUNT(*) AS courseCount
                  FROM courses
                  WHERE active = true
              )
              SELECT
                  u.userCount,
                  t.teacherCount,
                  s.studentCount,
                  c.courseCount
              FROM userCount u, teacherCount t, studentCount s, courseCount c""")
    StatisticDto getStatistics();
    @Query("""
    SELECT u FROM User u
    WHERE (
        LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) OR
        LOWER(u.firstname) LIKE LOWER(CONCAT('%', :query, '%')) OR
        LOWER(u.lastname) LIKE LOWER(CONCAT('%', :query, '%')) OR
        LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%'))
    )
    AND u.verified = true
""")
    List<User> searchByUsernameAndFirstNameAndLastnameAndEmail(@Param("query") String query);

    @Query("""
                SELECT u FROM User u
                WHERE (
                    LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))
                )
                AND u.verified = true
            """)
    List<User> searchByUsername(@Param("query") String query);

}