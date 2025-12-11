package uz.codebyz.onlinecoursebackend.promocode.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.codebyz.onlinecoursebackend.promocode.entity.PromoCode;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PromoCodeRepository extends JpaRepository<PromoCode, UUID> {
    @Query("select p from PromoCode p where (p.id=:id and p.active=true )")
    Optional<PromoCode> adminFindById(@Param("id") UUID promoCodeId);


    @Query("select p from PromoCode p where (p.code=:code)")
    Optional<PromoCode> findByCode(@Param("code") String code);


    @Query("select count(pc) > 0 from PromoCode pc where pc.code = :code")
    boolean existsByCode(@Param("code") String code);

    @Query("select pc from PromoCode pc where (pc.active=true ) order by pc.created")
    List<PromoCode> adminGetAllPromoCodes();

    @Query("select pc from PromoCode pc where (pc.active=true ) order by pc.created")
    Page<PromoCode> adminGetAllPromoCodes(Pageable pageable);


    @Query("select pc from PromoCode pc where pc.course.id = :courseId")
    Page<PromoCode> adminGetAllPromoCodeByCourseId(@Param("courseId") UUID courseId, Pageable pageable);

    @Query("select pc from PromoCode pc where pc.course.id = :courseId")
    List<PromoCode> adminGetAllPromoCodeByCourseId(@Param("courseId") UUID courseId);


    @Query("""
                select pc
                from PromoCode pc
                join pc.user u
                join u.teacher t
                where (t.id = :teacherId)
            """)
    Page<PromoCode> adminGetAllPromoCodeByTeacherId(@Param("teacherId") Long teacherId, Pageable pageable);

    @Query("""
                select pc
                from PromoCode pc
                join pc.user u
                join u.teacher t
                where (t.id = :teacherId)
            """)
    List<PromoCode> adminGetAllPromoCodeByTeacherId(@Param("teacherId") Long teacherId);

    /// /////////teacher uchun
    @Query("select p from PromoCode p where (p.id=:id)")
    Optional<PromoCode> teacherFindById(@Param("id") UUID promoCodeId);
    @Query("select pc from PromoCode pc where (pc.user.id=:userid and pc.active=true) order by pc.created")
    Page<PromoCode> teacherGetAllPromoCodes(@Param("userid") UUID userid, Pageable pageable);
}
