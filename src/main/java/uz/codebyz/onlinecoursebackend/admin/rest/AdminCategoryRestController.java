package uz.codebyz.onlinecoursebackend.admin.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.codebyz.onlinecoursebackend.admin.category.dto.AdminCategoryCreateRequest;
import uz.codebyz.onlinecoursebackend.admin.category.dto.AdminCategoryResponseDto;
import uz.codebyz.onlinecoursebackend.admin.category.dto.AdminCategoryUpdateRequest;
import uz.codebyz.onlinecoursebackend.admin.category.service.AdminCategoryService;
import uz.codebyz.onlinecoursebackend.category.entity.CategoryStatus;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoryRestController {

    private final AdminCategoryService adminCategoryService;

    public AdminCategoryRestController(AdminCategoryService adminCategoryService) {
        this.adminCategoryService = adminCategoryService;
    }

    // ==============================
    //         GET ALL (pagination)
    // ==============================
    @Operation(
            summary = "Barcha kategoriyalar ro‘yxatini olish (pagination bilan)",
            description = """
                    Ushbu endpoint tizimdagi barcha faol va o‘chirib tashlanmagan kategoriyalar ro‘yxatini 
                    pagination (sahifalash) orqali qaytaradi.
                    
                    Qaytariladigan kategoriyalar quyidagi shartlarga mos bo‘ladi:
                    active = true  → Kategoriya faol bo‘lishi kerak
                    deleted = false → Soft-delete qilinmagan bo‘lishi kerak
                    Natijalar yaratilgan vaqti (created) bo‘yicha tartiblanadi
                    page=0 dan boshlanadi
                    Pagination haqida:
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Faol va o‘chirib tashlanmagan kategoriyalar muvaffaqiyatli qaytarildi."
    )
    @GetMapping("/pagination")
    public ResponseDto<Page<AdminCategoryResponseDto>> getAllCategories(
            @RequestParam(defaultValue = "0", name = "page") int page,

            @RequestParam(defaultValue = "10", name = "size") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return adminCategoryService.getAllAdminCategories(pageable);
    }

    @Operation(
            summary = "Barcha kategoriyalar ro‘yxatini olish (paginationsiz)",
            description = """
                    Ushbu endpoint tizimdagi barcha faol va o‘chirib tashlanmagan kategoriyalarni 
                    to‘liq ro‘yxat ko‘rinishida qaytaradi.
                    
                    Qaytariladigan kategoriyalar quyidagi shartlarga mos bo‘ladi:
                    - active = true  → Kategoriya faol holatda bo‘lishi kerak
                    - deleted = false → Kategoriya soft-delete qilinmagan bo‘lishi kerak
                    
                    Ushbu API:
                    - Kategoriyalar soni kam bo‘lgan loyihalarda
                    - Dropdown / Select uchun barcha kategoriyalarni olishda
                    - Frontendda filter yoki tree shaklidagi menu yaratishda
                    - SEO URL larni generatsiya qilishda
                    - Kurs qo‘shish (Course Add Form) sahifasida kategoriya tanlashda
                    
                    juda foydali.
                    """
    )
    @GetMapping("/list")
    public ResponseDto<List<AdminCategoryResponseDto>> getAllCategories() {
        return adminCategoryService.getAllAdminCategories();
    }


    // ==============================
    //           SEARCH
    // ==============================
    @Operation(
            summary = "Kategoriya qidirish (pagination bilan)",
            description = """
                    Ushbu endpoint kategoriya ma’lumotlarini matn bo‘yicha qidirish imkonini beradi.
                    Qidiruv quyidagi maydonlar asosida amalga oshiriladi:
                    
                    - name → Kategoriya nomi
                    - slug → SEO URL identifikatori
                    - description → Kategoriyaning qisqa tavsifi
                    """
    )
    @GetMapping("/pagination/search")
    public ResponseDto<Page<AdminCategoryResponseDto>> search(
            @RequestParam(name = "kyw") String keyword,

            @RequestParam(defaultValue = "0",name = "page") int page,

            @RequestParam(defaultValue = "10",name = "size") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return adminCategoryService.searchCategories(keyword, pageable);
    }


    @Operation(
            description = """
                    Ushbu endpoint kategoriya ma’lumotlarini matn bo‘yicha qidirish uchun ishlatiladi.
                    Qidiruv quyidagi maydonlar bo‘yicha amalga oshiriladi:
                    
                    - name → Kategoriya nomi
                    - slug → SEO-friendly URL identifikatori
                    - description → Kategoriyaning tavsifi
                    
                    """
    )
    @GetMapping("/search")
    public ResponseDto<List<AdminCategoryResponseDto>> search(
            @RequestParam("kyw") String keyword
    ) {
        return adminCategoryService.searchCategories(keyword);
    }


    // ==============================
    //        DELETED LIST
    // ==============================
    @Operation(
            summary = "O‘chirilgan kategoriyalar (pagination bilan)",
            description = "deleted=true bo‘lgan barcha kategoriyalarni pagination bilan qaytaradi."
    )
    @GetMapping("/pagination/deleted-categories")
    public ResponseDto<Page<AdminCategoryResponseDto>> getAllDeleted(
            @RequestParam(defaultValue = "0",name = "page") int page,
            @RequestParam(defaultValue = "10",name = "size") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return adminCategoryService.getAllDeletedCategories(pageable);
    }

    @Operation(
            summary = "O‘chirilgan kategoriyalar (to‘liq ro‘yxat)",
            description = "deleted=true bo‘lgan kategoriyalarni to‘liq ro‘yxat ko‘rinishida qaytaradi."
    )
    @GetMapping("/deleted-categories")
    public ResponseDto<List<AdminCategoryResponseDto>> getAllDeleted() {
        return adminCategoryService.getAllDeletedCategories();
    }


    // ==============================
    //       SOFT DELETE LIST
    // ==============================
    @Operation(
            summary = "Soft-delete qilingan kategoriyalar (pagination bilan)",
            description = "active=false va deleted=true bo‘lgan kategoriyalarni pagination bilan qaytaradi."
    )
    @GetMapping("/pagination/soft-deleted")
    public ResponseDto<Page<AdminCategoryResponseDto>> getAllSoftDeleted(
            @RequestParam(defaultValue = "0",name = "page") int page,
             @RequestParam(defaultValue = "10",name = "size") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return adminCategoryService.getAllSoftDeleteCategories(pageable);
    }

    @Operation(
            summary = "Soft-delete qilingan kategoriyalar (to‘liq ro‘yxat)",
            description = "active=false va deleted=true bo‘lgan kategoriyalarni paginationsiz qaytaradi."
    )
    @GetMapping("/soft-deleted")
    public ResponseDto<List<AdminCategoryResponseDto>> getAllSoftDeleted() {
        return adminCategoryService.getAllSoftDeleteCategories();
    }


    // ==============================
    //        GET BY ID
    // ==============================
    @Operation(
            summary = "ID orqali kategoriya olish",
            description = "Kategoriyani uning ID bo‘yicha topib, to‘liq ma’lumotlarini qaytaradi."
    )
    @GetMapping("/category/{id}")
    public ResponseEntity<ResponseDto<AdminCategoryResponseDto>> getById(
            @PathVariable(name = "id") UUID id
    ) throws Exception {
        return ResponseEntity.ok(adminCategoryService.getCategoryById(id));
    }


    // ==============================
    //        CREATE CATEGORY
    // ==============================
    @Operation(
            summary = "Yangi kategoriya yaratish",
            description = "Yangi kategoriya qo‘shadi. Parent category bo‘lishi mumkin. Slug avtomatik yoki manual berilishi mumkin."
    )
    @PostMapping("/add-category")
    public ResponseDto<AdminCategoryResponseDto> create(
            @RequestBody AdminCategoryCreateRequest request
    ) {
        return adminCategoryService.createCategory(request);
    }


    // ==============================
    //        UPDATE CATEGORY
    // ==============================
    @Operation(
            summary = "Kategoriyani yangilash",
            description = "Mavjud kategoriyaning nomi, slug, description yoki parentini yangilaydi."
    )
    @PutMapping("/update/{categoryId}")
    public ResponseDto<AdminCategoryResponseDto> update(
            @PathVariable(name = "categoryId") UUID id,
            @RequestBody AdminCategoryUpdateRequest request
    ) {
        return adminCategoryService.updateCategory(id, request);
    }


    // ==============================
    //      STATUS o‘zgartirish
    // ==============================
    @Operation(
            summary = "Kategoriya statusini o‘zgartirish",
            description = "Kategoriya ACTIVE, INACTIVE, ARCHIVED va boshqa statuslarga o‘tkaziladi."
    )
    @PutMapping("/{categoryId}/status")
    public ResponseDto<AdminCategoryResponseDto> changeStatus(
            @PathVariable(name = "categoryId") UUID categoryId,
            @RequestParam(name = "status") CategoryStatus status
    ) {
        return adminCategoryService.changeCategoryStatus(categoryId, status);
    }


    // ==============================
    //      SLUG CHECK
    // ==============================
    @Operation(
            summary = "Slug bandligini tekshirish",
            description = "Slug bir xil bo‘lsa false, bo‘sh bo‘lsa true qaytaradi."
    )
    @GetMapping("/slug/check")
    public ResponseDto<Boolean> checkSlug(
             @RequestParam(name = "slug") String slug
    ) {
        return adminCategoryService.checkSlug(slug);
    }

    @Operation(
            summary = "Slug orqali kategoriyani olish",
            description = """
                    Ushbu endpoint berilgan slug qiymati bo‘yicha kategoriyani qaytaradi.
                    
                    Slug — kategoriyaning URL formatidagi noyob identifikatori bo‘lib,
                    foydalanuvchilar URL orqali kategoriya sahifasiga kirganda ishlatiladi.
                    
                    - Agar slug mavjud bo‘lsa → to‘liq kategoriya ma’lumotlari qaytariladi.
                    - Agar slug topilmasa → 'Category not found' xabari qaytariladi.
                    
                    Ushbu API odatda frontend tomonda kategoriya sahifalarini generatsiya qilishda,
                    SEO va SEO-friendly URL larni boshqarishda qo‘llaniladi.
                    """
    )
    @ApiResponse(responseCode = "200", description = "Kategoriya muvaffaqiyatli topildi va qaytarildi.")
    @ApiResponse(responseCode = "404", description = "Berilgan slug bo‘yicha kategoriya topilmadi.")
    @GetMapping("/slug/{slug}")
    public ResponseDto<AdminCategoryResponseDto> getSlug(
            @PathVariable(name = "slug") String slug
    ) {
        return adminCategoryService.getBySlug(slug);
    }


    @Operation(
            summary = "Kategoriyani soft-delete qilish",
            description = "Kategoriyani o‘chiradi, lekin ma’lumotlar bazadan butunlay o‘chirilmaydi. " +
                    "Kategoriyaning active=false va deleted=true holatlari o‘rnatiladi. " +
                    "Keyinchalik istalgan payt tiklash (restore) mumkin."
    )
    @DeleteMapping("/soft-delete-category/{category-id}")
    public ResponseDto<Void> deleteCategory(
            @PathVariable(name = "category-id") UUID cid
    ) {
        return adminCategoryService.softDelete(cid);
    }


    @Operation(
            summary = "Kategoriyani butunlay (permanent) o‘chirish",
            description = "Soft-delete qilingan kategoriyani ma’lumotlar bazasidan butunlay o‘chiradi. " +
                    "Bu amaldan so‘ng kategoriyani qayta tiklashning iloji bo‘lmaydi. " +
                    "Faqat oldindan soft-delete bo‘lgan kategoriyani o‘chiradi."
    )
    @DeleteMapping("/delete-permanently-category/{cid}")
    public ResponseDto<Void> deletePermanentlyCategory(
            @PathVariable(name = "cid") UUID cid
    ) {
        return adminCategoryService.deletePermanently(cid);
    }


    @GetMapping("/filter-by-status-pagination/{status}")
    @Operation(
            summary = "Filter categories by status with pagination",
            description = "Berilgan statusga mos kategoriyalar ro‘yxatini sahifalangan (pagination) "
                    + "ko‘rinishda qaytaradi. Natijalarni bo‘lish uchun page va size parametrlarini "
                    + " sifatida berish kerak."
    )
    public ResponseDto<Page<AdminCategoryResponseDto>> filterByStatusPagination(
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size,
            @PathVariable(name = "status") CategoryStatus status
    ) {
        return adminCategoryService.getAllByStatus(status, page, size);
    }


    @GetMapping("/filter-by-status/{status}")
    @Operation(
            summary = "Status bo‘yicha kategoriyalarni filtrlash",
            description = "Berilgan statusga mos keladigan barcha kategoriyalar ro‘yxatini qaytaradi. "
                    + "Bu endpoint paginatsiyasiz, to‘liq ro‘yxatni chiqaradi."
    )
    public ResponseDto<List<AdminCategoryResponseDto>> filterByStatus(
            @PathVariable(name = "status") CategoryStatus status
    ) {
        return adminCategoryService.getAllByStatus(status);
    }
}
