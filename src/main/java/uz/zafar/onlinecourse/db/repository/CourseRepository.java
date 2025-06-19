package uz.zafar.onlinecourse.db.repository;

//import com.example.onlinecoursemain.db.domain.Course;
//import com.example.onlinecoursemain.dto.date.DateDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.zafar.onlinecourse.db.domain.Course;
import uz.zafar.onlinecourse.dto.date.DateDto;

import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {
    @Query("select c  from Course  c where c.active=:active order by c.created")
    Page<Course> getCourses(Pageable pageable, @Param("active") Boolean active);

    @Query("""
            select
            round(extract(year from c.created)) as year ,
            round(extract(month from c.created)) as month ,
            round(extract(day from c.created)) as day ,
            round(extract(minute from c.created)) as minute ,
            round(extract(hour from c.created)) as hour ,
            round(extract(second from c.created)) as second
            from Course c where c.id=:pkey and c.active=true""")
    Optional<DateDto> getCourseOfCreatedDate(@Param("pkey") UUID pkey);

    @Query("""
            select
            round(extract(year from c.updated)) as year ,
            round(extract(month from c.updated)) as month ,
            round(extract(day from c.updated)) as day ,
            round(extract(minute from c.updated)) as minute ,
            round(extract(hour from c.updated)) as hour ,
            round(extract(second from c.updated)) as second
            from Course c where c.id=:pkey and c.active=true""")
    Optional<DateDto> getCourseOfUpdatedDate(@Param("pkey") UUID pkey);

    @Query("select c from Course c where c.id=:pkey and c.active=true")
    Optional<Course> findByCourseId(@Param("pkey") UUID pkey);
    @Query("select c from Course c where c.name=:name and c.active=true")
    Optional<Course> findByCourseName(@Param("name") String name);



//    Page<Course>getAllCourses(Pageable pageable);
}
