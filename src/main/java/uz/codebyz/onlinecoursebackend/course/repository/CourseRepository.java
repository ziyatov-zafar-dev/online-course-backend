package uz.codebyz.onlinecoursebackend.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.course.entity.CourseStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {
    @Query("select c from Course c where (c.deleted=false and c.active=true and c.id=:cid) order by c.orderNumber")
    Optional<Course> findAdminByCourseId(@Param("cid") UUID courseId);

    @Query("""
            select c from Course c where (c.active=true and c.deleted=false)
            order by c.orderNumber
            """)
    Page<Course> getAdminAllCourses(Pageable pageable);

    @Query("""
            select c from Course c where (c.active=true and c.deleted=false)
            order by c.orderNumber
            """)
    List<Course> getAdminAllCourses();

    @Query("""
            select c from Course c where (
                        c.active=true and c.deleted=false
                                    and c.status=:status
                                    )
            order by c.orderNumber
            """)
    Page<Course> getAdminAllByStatus(@Param("status") CourseStatus status, Pageable pageable);

    @Query("""
            select c from Course c where (
                        c.active=true and c.deleted=false
                                    and c.status=:status
                                    )
            order by c.orderNumber
            """)
    List<Course> getAdminAllByStatus(@Param("status") CourseStatus status);

    @Query("select count(c) > 0 from Course c where (c.id = :id and c.active=true and c.deleted=false)")
    boolean existsCourseById(@Param("id") UUID id);

    @Query("select count(c) > 0 from Course c where (c.slug = :slug and c.active=true and c.deleted=false)")
    boolean existsBySlug(@Param("slug") String slug);

    @Query("select c from Course c where (c.active=true and c.deleted=false and c.slug=:slug)")
    Optional<Course> findAdminBySlug(@Param("slug") String slug);
}
