package uz.codebyz.onlinecoursebackend.category.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.codebyz.onlinecoursebackend.category.entity.Category;
import uz.codebyz.onlinecoursebackend.category.entity.CategoryStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Query("select c from Category c where (c.active=true  and c.deleted=false) order by c.orderNumber")
    public List<Category> getAdminAllCategories();

    @Query("select c from Category c where (c.active=true  and c.deleted=false) order by c.orderNumber")
    public Page<Category> getAdminAllCategories(Pageable pageable);

    @Query("select c from Category c where (c.active=true and c.deleted=false and c.id=:id)")
    public Optional<Category> findAdminByCategoryId(@Param("id") UUID categoryId);

    @Query("select c from Category c where (c.active=false and c.deleted=false and c.id=:id)")
    public Optional<Category> getAdminSoftDeleteCategoryById(@Param("id") UUID categoryId);

    @Query("select c from Category c where (c.active=true and c.deleted=false and c.slug=:slug)")
    public Optional<Category> findAdminBySlug(@Param("slug") String slug);

    @Modifying
    @Query("UPDATE Category c SET c.status = :status WHERE (c.id = :id and c.active=true and c.deleted=false)")
    int updateStatus(@Param("id") UUID id,
                     @Param("status") CategoryStatus status);

    @Query("select c from Category c where (c.slug=:slug)")
    Optional<Category> checkAdminSlug(String slug);

    @Query("select c from Category c where (c.slug=:slug and c.active=true and c.deleted=false)")
    Optional<Category> getAdminBySlug(String slug);
    @Query("""
       SELECT c
       FROM Category c 
       WHERE c.deleted = false
         AND (
                LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(c.slug)   LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
                   order by c.orderNumber
       """)
    List<Category> searchAdminCategories(@Param("keyword") String text);
    @Query("""
       SELECT c
       FROM Category c 
       WHERE c.deleted = false
         AND (
                LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(c.slug)   LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
                   order by c.orderNumber
       """)
    Page<Category> searchAdminCategories(@Param("keyword") String text, Pageable pageable);

    @Query("select c from Category c where (c.deleted=:deleted and c.active=:active)")
    List<Category> findAdminAllByActive(@Param("active") boolean active, @Param("deleted") boolean deleted);

    @Query("select c from Category c where (c.deleted=:deleted and c.active=:active)")
    Page<Category> findAdminAllByActive(@Param("active") boolean active, @Param("deleted") boolean deleted, Pageable pageable);
    @Query("select c from Category c where (c.active=true and c.deleted=false and c.status=:status)")
    public Page<Category> findAdminAllActiveByStatus(@Param("status") CategoryStatus status, Pageable pageable);

    @Query("select c from Category c where (c.active=true and c.deleted=false and c.status=:status)")
    public List<Category> findAdminAllActiveByStatus(@Param("status") CategoryStatus status);

    /// ///////////////teachers
    ///
    @Query("select c from Category c where" +
            " (c.active=true and c.deleted=false and " +
            "c.status=uz.codebyz.onlinecoursebackend.category.entity.CategoryStatus.OPEN)" +
            " order by c.orderNumber asc")
    public List<Category>teacherFindAllCategories();
    @Query("select c from Category c where c.slug=:slug")
    public Optional<Category>teacherFindBySlug(@Param("slug") String slug);
    @Query("select c from Category c where(c.active=true  and c.deleted = false and c.id=:id) ")
    public Optional<Category> teacherFindById(@Param("id") UUID id);
}
