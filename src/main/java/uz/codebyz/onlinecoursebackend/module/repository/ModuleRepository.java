package uz.codebyz.onlinecoursebackend.module.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.codebyz.onlinecoursebackend.module.entity.Module;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<Module, UUID> {
    @Query("select m from Module m where (m.id=:moduleId and m.active=true and m.deleted=false)")
    Optional<Module> findByModuleId(@Param("moduleId") UUID moduleId);

    @Query("select m from Module m where (m.course.id=:courseId and m.active=true and m.deleted=false) order by m.orderNumber asc")
    Page<Module> findAllByCourseId(@Param("courseId") UUID courseId, Pageable pageable);

    @Query("select m from Module m where (m.course.id=:courseId and m.active=true and m.deleted=false) order by m.orderNumber asc")
    List<Module> findAllByCourseId(@Param("courseId") UUID courseId);

    @Query("select m from Module m where (m.course.id=:courseId and m.active=false and m.deleted=false) order by m.orderNumber asc")
    List<Module> findAllSoftDeletedModules(@Param("courseId") UUID courseId);

    @Query("select m from Module m where (m.course.id=:courseId and m.active=false and m.deleted=false) order by m.orderNumber asc")
    Page<Module> findAllSoftDeletedModules(@Param("courseId") UUID courseId, Pageable pageable);

    @Query("select m from Module m where (m.course.id=:courseId and m.active=false and m.deleted=true) order by m.orderNumber asc")
    List<Module> findAllHardDeletedModules(@Param("courseId") UUID courseId);
    @Query("select m from Module m where (m.course.id=:courseId and m.active=false and m.deleted=true) order by m.orderNumber asc")
    Page<Module> findAllHardDeletedModules(@Param("courseId") UUID courseId, Pageable pageable);

    @Query("select m from Module m where m.id=:id")
    Optional<Module>findById(@Param("id") UUID moduleId);
}
