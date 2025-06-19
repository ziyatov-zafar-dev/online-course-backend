package uz.zafar.onlinecourse.db.repository;

//import com.example.onlinecoursemain.db.domain.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.zafar.onlinecourse.db.domain.Lesson;
import uz.zafar.onlinecourse.dto.date.DateDto;

import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, UUID> {
    @Query("""
            select
            round(extract(year from g.updated)) as year ,
            round(extract(month from g.updated)) as month ,
            round(extract(day from g.updated)) as day ,
            round(extract(minute from g.updated)) as minute ,
            round(extract(hour from g.updated)) as hour ,
            round(extract(second from g.updated)) as second
            from Lesson g where g.id=:pkey and g.active=true""")
    DateDto getLessonCreatedDate(@Param("pkey") UUID pkey);

    @Query("""
            select
            round(extract(year from g.created)) as year ,
            round(extract(month from g.created)) as month ,
            round(extract(day from g.created)) as day ,
            round(extract(minute from g.created)) as minute ,
            round(extract(hour from g.created)) as hour ,
            round(extract(second from g.created)) as second
            from Lesson g where g.id=:pkey and g.active=true""")
    DateDto getLessonUpdatedDate(@Param("pkey") UUID pkey);

    @Query("SELECT l FROM Lesson l WHERE l.group.id = :groupId")
    Page<Lesson> getLessonsByGroupId(@Param("groupId") UUID groupId, Pageable pageable);

}
