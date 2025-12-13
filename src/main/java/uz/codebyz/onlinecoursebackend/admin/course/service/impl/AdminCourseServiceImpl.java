package uz.codebyz.onlinecoursebackend.admin.course.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.codebyz.onlinecoursebackend.admin.category.mapper.AdminPromoCodeMapper;
import uz.codebyz.onlinecoursebackend.admin.category.promoCodeDtos.AdminCourseAndPromoCodeResponseDto;
import uz.codebyz.onlinecoursebackend.admin.category.promoCodeDtos.AdminCreatePromoCodeRequestDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.*;
import uz.codebyz.onlinecoursebackend.admin.course.mapper.AdminCourseMapper;
import uz.codebyz.onlinecoursebackend.admin.course.service.AdminCourseService;
import uz.codebyz.onlinecoursebackend.category.entity.Category;
import uz.codebyz.onlinecoursebackend.category.repository.CategoryRepository;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.course.entity.Course;
import uz.codebyz.onlinecoursebackend.course.entity.CourseStatus;
import uz.codebyz.onlinecoursebackend.course.repository.CourseRepository;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;
import uz.codebyz.onlinecoursebackend.helper.FileHelper;
import uz.codebyz.onlinecoursebackend.promocode.entity.PromoCode;
import uz.codebyz.onlinecoursebackend.promocode.repository.PromoCodeRepository;
import uz.codebyz.onlinecoursebackend.teacher.entity.Teacher;
import uz.codebyz.onlinecoursebackend.teacher.repository.TeacherRepository;
import uz.codebyz.onlinecoursebackend.userDevice.service.UserDeviceService;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminCourseServiceImpl implements AdminCourseService {
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final TeacherRepository teacherRepository;
    private final UserDeviceService userDeviceService;
    @Value("${course.image.url}")
    private String baseCourseImagePath;
    @Value("${course.video.url}")
    private String baseCourseVideoPath;

    public AdminCourseServiceImpl(CourseRepository courseRepository, CategoryRepository categoryRepository, PromoCodeRepository promoCodeRepository, TeacherRepository teacherRepository, UserDeviceService userDeviceService) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.promoCodeRepository = promoCodeRepository;
        this.teacherRepository = teacherRepository;
        this.userDeviceService = userDeviceService;
    }

    //    @Override
//    public ResponseDto<AdminCourseResponseDto> createCourse(AdminCourseCreateRequestDto request) {
//        Optional<Category> cOp = categoryRepository.findAdminByCategoryId(request.getCategoryId());
//        if (cOp.isEmpty())
//            throw new RuntimeException("Category not found");
//        Course course = AdminCourseMapper.toEntityFromCreateRequest(request);
//        course.setCategory(cOp.get());
//        MultipartFile video = request.getVideo();
//        MultipartFile image = request.getImage();
//        ResponseDto<UploadFileResponseDto> checkUploadVideo = uploadFile(video, baseCourseVideoPath);
//        ResponseDto<UploadFileResponseDto> checkUploadImage = uploadFile(image, baseCourseImagePath);
//        if (!checkUploadVideo.isSuccess()) {
//            throw new RuntimeException("Video ni saqlashda xatolik yuz berdi: " + checkUploadVideo.getMessage());
//        }
//
//        if (!checkUploadImage.isSuccess()) {
//            throw new RuntimeException("Video ni saqlashda xatolik yuz berdi: " + checkUploadVideo.getMessage());
//        }
//        UploadFileResponseDto savedVideo = checkUploadVideo.getData();
//        UploadFileResponseDto savedImage = checkUploadImage.getData();
//
//        course.setImgName(savedImage.getFileName());
//        course.setImgUrl(savedImage.getFileUrl());
//        course.setImgSize(savedImage.getFileSize());
//        course.setPromoCourseVideoFileName(savedVideo.getFileName());
//        course.setPromoCourseVideoUrl(savedVideo.getFileUrl());
//        course.setPromoCourseVideoFileSize(savedVideo.getFileSize());
//        return new ResponseDto<>(true, "Success", AdminCourseMapper.toDto(courseRepository.save(course)));
//    }

    @Override
    public ResponseDto<AdminCourseResponseDto> addVideo(UUID courseId, MultipartFile video) {
        Optional<Course> cOp = courseRepository.findById(courseId);
        if (cOp.isEmpty()) {
            return ResponseDto.error("Bunday ID li kurs mavjud emas. kurs id: " + courseId);
        }
        Course course = cOp.get();

        ResponseDto<UploadFileResponseDto> videoUpload = uploadFile(video, baseCourseVideoPath, true);
        if (!videoUpload.isSuccess())
            return ResponseDto.error("Video yuklashda xatolik: " + videoUpload.getMessage());
        if (FileHelper.deleteFile(course.getPromoCourseVideoUrl())) {
            System.out.println("Fayl o'chirildi");
        }

        UploadFileResponseDto uploadFile = videoUpload.getData();
        if (course.getPromoCourseVideoUrl() != null) {
            FileHelper.deleteFile(course.getPromoCourseVideoUrl());
        }
        course.setPromoCourseVideoFileSize(uploadFile.getFileSize());
        course.setPromoCourseVideoFileName(uploadFile.getFileName());
        course.setPromoCourseVideoUrl(uploadFile.getFileUrl());
        return new ResponseDto<>(true, "Success", AdminCourseMapper.toDto(courseRepository.save(course)));
    }

    @Override
    public ResponseDto<AdminCourseResponseDto> addImage(UUID courseId, MultipartFile image) {
        Optional<Course> cOp = courseRepository.findById(courseId);
        if (cOp.isEmpty()) {
            return ResponseDto.error("Bunday ID li kurs mavjud emas. kurs id: " + courseId);
        }
        Course course = cOp.get();
        ResponseDto<UploadFileResponseDto> videoUpload = uploadFile(image, baseCourseImagePath, false);
        if (!videoUpload.isSuccess())
            return ResponseDto.error("Video yuklashda xatolik: " + videoUpload.getMessage());
        if (FileHelper.deleteFile(course.getImgUrl())) {
            System.out.println("Fayl o'chirildi");
        }
        UploadFileResponseDto uploadFile = videoUpload.getData();
        if (course.getImgUrl() != null) {
            FileHelper.deleteFile(course.getImgUrl());
        }
        course.setImgSize(uploadFile.getFileSize());
        course.setImgName(uploadFile.getFileName());
        course.setImgUrl(uploadFile.getFileUrl());
        return new ResponseDto<>(true, "Success", AdminCourseMapper.toDto(courseRepository.save(course)));
    }

    /*@Override
    public ResponseDto<AdminCourseResponseDto> createCourse(AdminCourseCreateRequestDto request) {
        Optional<Course> courseOptional = courseRepository.findAdminBySlug(request.getSlug());
        if (courseOptional.isPresent()) {
            return new ResponseDto<>(false, "Bu slug band. Siz kiritgan slug: " + request.getSlug());
        }
        Optional<Teacher> tOp = teacherRepository.findById(request.getTeacherId());
        if (tOp.isEmpty())
            return new ResponseDto<>(false, "Bunday ID li teacher topilmadi. Teacher ID: " + request.getTeacherId());
        // 1) Category tekshirish
        Optional<Category> cOp = categoryRepository.findAdminByCategoryId(request.getCategoryId());
        if (cOp.isEmpty())
            return ResponseDto.error("Kategoriya topilmadi!");
        Category category = cOp.get();
        // 2) Pricing validatsiya (discount bilan bo‘lsa)
        if (request.getDiscountPrice() != null && request.getDiscountPercent() != null)
            return ResponseDto.error("Chegirma summasi va chegirma foizi bir vaqtda berilmaydi!");

        if (request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) <= 0)
            return ResponseDto.error("Kurs narxi 0 dan katta bo‘lishi kerak!");

        // 3) ENTITY yaratish
        Course course = AdminCourseMapper.toEntityFromCreateRequest(request);
        course.setCategory(category);
        course.setTeacher(tOp.get());
        // 1) Slug bo'sh bo‘lsa yoki null bo‘lsa → xato
        if (request.getSlug() == null || request.getSlug().isBlank()) {
            return ResponseDto.error("Slug bo‘sh bo‘lishi mumkin emas!");
        }

// 2) Slug formatini tekshirish
        String slugRegex = "^[a-z0-9]+(?:-[a-z0-9]+)*$";

        if (!request.getSlug().matches(slugRegex)) {
            return ResponseDto.error(
                    "Slug noto‘g‘ri formatda! To‘g‘ri format: faqat kichik harflar (a–z), raqamlar (0–9) va tire (-). " +
                            "Masalan: 'java-kurs', 'backend-bootcamp', 'spring-boot-101'"
            );
        }

        // 6) Chegirma logikasi — yangi kursnarxni hisoblash
        BigDecimal finalPrice = request.getFinalPrice();
        BigDecimal price = request.getPrice();

        if (finalPrice != null) {
            // Eng ustuvor
            course.setFinalPrice(finalPrice);
            course.setDiscountPercent(null);
            course.setDiscountPrice(null);
        } else if (request.getDiscountPercent() != null) {
            BigDecimal percent =
                    price.multiply(BigDecimal.valueOf(request.getDiscountPercent()))
                            .divide(BigDecimal.valueOf(100));
            course.setFinalPrice(price.subtract(percent));
        } else if (request.getDiscountPrice() != null) {
            course.setFinalPrice(price.subtract(request.getDiscountPrice()));
        } else {
            course.setFinalPrice(price);
        }

        // 7) Sana
        course.setCreated(CurrentTime.currentTime());
        course.setUpdated(CurrentTime.currentTime());
        course.setPromoCodes(new ArrayList<>());
        course.setSlug(request.getSlug());
        // 8) Saqlash
        Course saved = courseRepository.save(course);

        return ResponseDto.ok("Kurs muvaffaqiyatli yaratildi!", AdminCourseMapper.toDto(saved));
    }*/

    @Override
    public ResponseDto<AdminCourseResponseDto> createCourse(AdminCourseCreateRequestDto request) {

        // 0) Slug unique
        if (courseRepository.findAdminBySlug(request.getSlug()).isPresent()) {
            return ResponseDto.error("Bu slug band. Siz kiritgan slug: " + request.getSlug());
        }

        // 1) Teacher
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Bunday ID li teacher topilmadi. Teacher ID: " + request.getTeacherId())
                );

        // 2) Category
        Category category = categoryRepository.findAdminByCategoryId(request.getCategoryId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Kategoriya topilmadi!")
                );

        // 3) Price majburiy
        BigDecimal price = request.getPrice();
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseDto.error("Kurs narxi 0 dan katta bo‘lishi kerak!");
        }

        // 4) Slug validatsiya
        if (request.getSlug() == null || request.getSlug().isBlank()) {
            return ResponseDto.error("Slug bo‘sh bo‘lishi mumkin emas!");
        }

        String slugRegex = "^[a-z0-9]+(?:-[a-z0-9]+)*$";
        if (!request.getSlug().matches(slugRegex)) {
            return ResponseDto.error(
                    "Slug noto‘g‘ri formatda! Masalan: 'java-backend', 'spring-boot-101'"
            );
        }

        // ================= PRICE LOGIC =================
        BigDecimal finalPrice;
        BigDecimal discountPrice;
        Integer discountPercent;

        if (request.getFinalPrice() != null) {
            // price + finalPrice → qolganlarini hisoblaymiz
            finalPrice = request.getFinalPrice();

            if (finalPrice.compareTo(price) > 0) {
                return ResponseDto.error("Final price asosiy narxdan katta bo‘lishi mumkin emas!");
            }

            discountPrice = price.subtract(finalPrice);

            discountPercent = discountPrice
                    .multiply(BigDecimal.valueOf(100))
                    .divide(price, 0, RoundingMode.HALF_UP)
                    .intValue();

        } else if (request.getDiscountPercent() != null) {
            // price + percent
            discountPercent = request.getDiscountPercent();

            if (discountPercent < 0 || discountPercent > 100) {
                return ResponseDto.error("Chegirma foizi 0–100 oralig‘ida bo‘lishi kerak!");
            }

            discountPrice = price
                    .multiply(BigDecimal.valueOf(discountPercent))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            finalPrice = price.subtract(discountPrice);

        } else if (request.getDiscountPrice() != null) {
            // price + discountPrice
            discountPrice = request.getDiscountPrice();

            if (discountPrice.compareTo(BigDecimal.ZERO) < 0 ||
                    discountPrice.compareTo(price) > 0) {
                return ResponseDto.error("Chegirma summasi noto‘g‘ri!");
            }

            finalPrice = price.subtract(discountPrice);

            discountPercent = discountPrice
                    .multiply(BigDecimal.valueOf(100))
                    .divide(price, 0, RoundingMode.HALF_UP)
                    .intValue();

        } else {
            // faqat price → chegirma yo‘q
            discountPrice = BigDecimal.ZERO;
            discountPercent = 0;
            finalPrice = price;
        }

        // ================= ENTITY =================
        Course course = AdminCourseMapper.toEntityFromCreateRequest(request);
        course.setCategory(category);
        course.setTeacher(teacher);
        if (request.getDiscountStartAt() == null || request.getDiscountEndAt() == null) {
            return new ResponseDto<>(false, "Chegirma boshlanish vaqti va tugash vaqtini kiritishingiz kerak");
        }
        course.setDiscountStartAt(request.getDiscountStartAt());
        course.setDiscountEndAt(request.getDiscountEndAt());
        course.setPrice(price);
        course.setFinalPrice(finalPrice);
        course.setDiscountPrice(discountPrice);
        course.setDiscountPercent(discountPercent);

        course.setCreated(CurrentTime.currentTime());
        course.setUpdated(CurrentTime.currentTime());
        course.setPromoCodes(new ArrayList<>());
        course.setSlug(request.getSlug());

        Course saved = courseRepository.save(course);

        return ResponseDto.ok(
                "Kurs muvaffaqiyatli yaratildi!",
                AdminCourseMapper.toDto(saved)
        );
    }


    private boolean checkSlug(String slug) {
        // 1) Slug bo'sh bo‘lsa yoki null bo‘lsa → xato
        if (slug == null || slug.isBlank()) {
            return false;
        }
        String slugRegex = "^[a-z0-9]+(?:-[a-z0-9]+)*$";
        return slug.matches(slugRegex);

    }

    private ResponseDto<UploadFileResponseDto> uploadFile(MultipartFile file, String folder, boolean isVideo) {

        try {
            if (isVideo) file = FileHelper.compressVideoAndReturn(file);
            if (file.isEmpty()) {
                return ResponseDto.error("Fayl topilmadi!");
            }
            if (!new File(folder).exists()) {
                new File(folder).mkdirs();
            }
            String originalName = file.getOriginalFilename();
            String newFileName = (CurrentTime.currentTime() + "_" + originalName).replaceAll("[^a-zA-Z0-9_.]", "_");
            Path filePath = Paths.get(folder + newFileName);
            Files.write(filePath, file.getBytes());
            return new ResponseDto<>(true, "Ok", new UploadFileResponseDto(
                    originalName,
                    folder + newFileName,
                    file.getSize()
            ));
        } catch (Exception e) {
            return new ResponseDto<>(false, "Kutilmagan xatolik" + e.getMessage());
        }
    }

    @Override
    public ResponseDto<AdminCourseResponseDto> updateCourse(UUID courseId, AdminCourseUpdateRequestDto request) {
        Optional<Course> cOp = courseRepository.findAdminByCourseId(courseId);
        if (cOp.isEmpty())
            throw new RuntimeException("Course not found");
        Course course = cOp.get();
        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setSlug(request.getSlug());
        course.setOrderNumber(request.getOrderNumber());
        course.setTelegramGroupLink(request.getTelegramGroupLink());
        course.setTelegramChannelLink(request.getTelegramChannelLink());
        course.setHasTelegramChannel(request.getHasTelegramChannel());
        course.setHasTelegramGroup(request.getHasTelegramGroup());
        course.setDiscountPrice(request.getDiscountPrice());
        course.setDiscountPercent(request.getDiscountPercent());
        course.setDiscountStartAt(request.getDiscountStartAt());
        course.setDiscountEndAt(request.getDiscountEndAt());
        course.setUpdatedAt();
        MultipartFile video = request.getVideo();
        MultipartFile image = request.getImage();
        ResponseDto<UploadFileResponseDto> checkUploadVideo = uploadFile(video, baseCourseVideoPath, true);
        ResponseDto<UploadFileResponseDto> checkUploadImage = uploadFile(image, baseCourseImagePath, false);
        if (!checkUploadVideo.isSuccess()) {
            throw new RuntimeException("Video ni saqlashda xatolik yuz berdi: " + checkUploadVideo.getMessage());
        }

        if (!checkUploadImage.isSuccess()) {
            throw new RuntimeException("Video ni saqlashda xatolik yuz berdi: " + checkUploadVideo.getMessage());
        }
        UploadFileResponseDto savedVideo = checkUploadVideo.getData();
        UploadFileResponseDto savedImage = checkUploadImage.getData();
        course.setImgName(savedImage.getFileName());
        course.setImgUrl(savedImage.getFileUrl());
        course.setImgSize(savedImage.getFileSize());
        course.setPromoCourseVideoFileName(savedVideo.getFileName());
        course.setPromoCourseVideoUrl(savedVideo.getFileUrl());
        course.setPromoCourseVideoFileSize(savedVideo.getFileSize());
        return new ResponseDto<>(true, "Success", AdminCourseMapper.toDto(courseRepository.save(course)));
    }

    @Override
    public ResponseDto<AdminCourseResponseDto> getCourseById(UUID courseId) {
        Optional<Course> cOp = courseRepository.findAdminByCourseId(courseId);
        if (cOp.isEmpty())
            throw new RuntimeException("Course not found id:  " + courseId);
        return new ResponseDto<>(true, "Success", AdminCourseMapper.toDto(courseRepository.save(cOp.get())));
    }

    @Override
    public ResponseDto<Page<AdminCourseResponseDto>> getAllCourses(int page, int size) {
        Page<Course> adminAllCourses = courseRepository.getAdminAllCourses(PageRequest.of(page, size));
        return new ResponseDto<>(true, "Success", AdminCourseMapper.toDtoPage(adminAllCourses));
    }

    @Override
    public ResponseDto<List<AdminCourseResponseDto>> getAllCourses() {
        List<Course> adminAllCourses = courseRepository.getAdminAllCourses();
        return new ResponseDto<>(true, "Success", AdminCourseMapper.toDtoList(adminAllCourses));
    }

    @Override
    public ResponseDto<Page<AdminCourseResponseDto>> getByStatus(CourseStatus status, int page, int size) {
        Page<Course> adminAllCourses = courseRepository.getAdminAllByStatus(status, PageRequest.of(page, size));
        return new ResponseDto<>(true, "Success", AdminCourseMapper.toDtoPage(adminAllCourses));
    }

    @Override
    public ResponseDto<List<AdminCourseResponseDto>> getByStatus(CourseStatus status) {
        List<Course> adminAllCourses = courseRepository.getAdminAllByStatus(status);
        return new ResponseDto<>(true, "Success", AdminCourseMapper.toDtoList(adminAllCourses));
    }

    @Override
    public ResponseDto<AdminCourseResponseDto> changeCourseStatus(UUID courseId, CourseStatus status) {
        Optional<Course> cOp = courseRepository.findAdminByCourseId(courseId);
        if (cOp.isEmpty())
            throw new RuntimeException("Course not found id:  " + courseId);
        Course course = cOp.get();
        if (course.getStatus() == status) {
            throw new RuntimeException("Avvalgi status bilan bir xil");
        }
        course.setStatus(status);
        course.setUpdatedAt();
        return new ResponseDto<>(true, "Success", AdminCourseMapper.toDto(courseRepository.save(course)));
    }

    @Override
    public ResponseDto<Void> softDelete(UUID courseId) {
        Optional<Course> cOp = courseRepository.findAdminByCourseId(courseId);
        if (cOp.isEmpty())
            throw new RuntimeException("Course not found id:  " + courseId);
        Course course = cOp.get();
        if (!course.getActive()) {
            if (!course.getDeleted()) {
                return new ResponseDto<>(false, "Bu kursni avval soft delete qismidan ham o'chirgan. tiklash ham o'chirish ham imkonsiz");
            }
            return new ResponseDto<>(false, "Bu kursni sizdan avval kimdir o'chirganga o'xshaydi");
        }
        course.setActive(false);
        course.setUpdatedAt();
        return new ResponseDto<>(true, "Muvaffaqiyatli o'chirildi, ammo qaytib tiklash mumkin", null);
    }

    @Override
    public ResponseDto<Void> hardDelete(UUID courseId) {
        Optional<Course> cOp = courseRepository.findAdminByCourseId(courseId);
        if (cOp.isEmpty())
            throw new RuntimeException("Course not found id:  " + courseId);
        Course course = cOp.get();
        if (!course.getActive() && !course.getDeleted()) {
            course.setDeleted(false);
            course.setActive(false);
            course.setUpdatedAt();
            return new ResponseDto<>(true, "Muvaffaqiyatli o'chirildi, ammo qaytib tiklash mumkin", null);

        }
        return new ResponseDto<>(false, "Bu kursni o'chirishdan oldin soft delete qilishingiz kerak. afsuski to'g'ridan to'gri o'chirib yuborishning iloji yo'q");
    }

    @Override
    public ResponseDto<AdminCourseResponseDto> updateCoursePricing(UUID courseId,
                                                                   AdminCoursePricingRequestDto request) {

        Optional<Course> cOp = courseRepository.findAdminByCourseId(courseId);
        if (cOp.isEmpty())
            return ResponseDto.error("Kurs topilmadi. ID: " + courseId);

        Course course = cOp.get();

        BigDecimal price = request.getPrice();
        BigDecimal discountPrice = request.getDiscountPrice();
        Integer discountPercent = request.getDiscountPercent();
        LocalDateTime start = request.getDiscountStartAt();
        LocalDateTime end = request.getDiscountEndAt();
        BigDecimal finalPrice = request.getFinalPrice();

        // 1) PRICE VALIDATION
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0)
            return ResponseDto.error("Kurs narxi 0 dan katta bo‘lishi shart!");

        // 2) Two discounts cannot be used at same time
        if (discountPrice != null && discountPercent != null)
            return ResponseDto.error("Chegirma summasi va chegirma foizi bir vaqtning o‘zida berilmaydi!");

        // 3) Time validation
        if (start != null && end != null && !end.isAfter(start))
            return ResponseDto.error("Chegirma tugash vaqti boshlanish vaqtidan keyin bo‘lishi shart!");

        BigDecimal calculatedFinal = null;

        // 4) FINAL PRICE PRIORITY
        if (finalPrice != null) {
            if (finalPrice.compareTo(BigDecimal.ZERO) <= 0)
                return ResponseDto.error("Final narx 0 dan katta bo‘lishi shart!");

            calculatedFinal = finalPrice;

            // chegirmalarni tozalash
            discountPrice = null;
            discountPercent = null;
        }

        // 5) Calculate by percent
        else if (discountPercent != null) {
            if (discountPercent < 0 || discountPercent > 100)
                return ResponseDto.error("Chegirma foizi 0 dan 100 gacha bo‘lishi mumkin!");

            BigDecimal percentValue =
                    price.multiply(BigDecimal.valueOf(discountPercent))
                            .divide(BigDecimal.valueOf(100));

            calculatedFinal = price.subtract(percentValue);
        }

        // 6) Calculate by discount price
        else if (discountPrice != null) {

            if (discountPrice.compareTo(BigDecimal.ZERO) < 0)
                return ResponseDto.error("Chegirma summasi manfiy bo‘lishi mumkin emas!");

            if (discountPrice.compareTo(price) > 0)
                return ResponseDto.error("Chegirma summasi kurs narxidan katta bo‘lishi mumkin emas!");

            calculatedFinal = price.subtract(discountPrice);
        }

        // 7) No discount
        else {
            calculatedFinal = price;
        }

        // 8) APPLY TO ENTITY
        course.setPrice(price);
        course.setDiscountPrice(discountPrice);
        course.setDiscountPercent(discountPercent);
        course.setDiscountStartAt(start);
        course.setDiscountEndAt(end);
        course.setFinalPrice(calculatedFinal);
        course.setUpdatedAt();
        return ResponseDto.ok("Kurs narxi muvaffaqiyatli yangilandi!", AdminCourseMapper.toDto(courseRepository.save(course)));
    }

    @Override
    public ResponseDto<AdminCourseResponseDto> restoreCourse(UUID courseId) {
        Optional<Course> cOp = courseRepository.findAdminByCourseId(courseId);
        if (cOp.isEmpty())
            return ResponseDto.error("Not Found Course id: " + courseId);
        Course course = cOp.get();
        if (course.getActive() && !course.getDeleted()) {
            return new ResponseDto<>(false, "Kurs allaqachon tiklangan");
        }
        if (course.getDeleted()) {
            return new ResponseDto<>(false, "Kursni tiklash imkonsiz. allaqachon o'chirilgan");
        }
        course.setActive(true);
        course.setDeleted(false);
        return new ResponseDto<>(true, "Ok", AdminCourseMapper.toDto(courseRepository.save(course)));
    }

    @Override
    public ResponseDto<AdminCourseAndPromoCodeResponseDto> findByPromoCodeId(UUID promoCodeId) {
        Optional<PromoCode> pOp = promoCodeRepository.adminFindById(promoCodeId);
        if (pOp.isEmpty())
            return ResponseDto.error("Not Found PromoCode id: " + promoCodeId);
        PromoCode promoCode = pOp.get();
        AdminCourseAndPromoCodeResponseDto response = AdminPromoCodeMapper.toDto(promoCode, userDeviceService);
        return new ResponseDto<>(true, "Ok", response);
    }

    @Override
    public ResponseDto<AdminCourseAndPromoCodeResponseDto> findByPromoCode(String code) {
        Optional<PromoCode> pOp = promoCodeRepository.findByCode(code);
        if (pOp.isEmpty())
            return ResponseDto.error("Not Found code id: " + code);
        PromoCode promoCode = pOp.get();
        AdminCourseAndPromoCodeResponseDto response = AdminPromoCodeMapper.toDto(promoCode, userDeviceService);
        return new ResponseDto<>(true, "Ok", response);
    }

    @Override
    public boolean existsByPromoCode(String code) {
        try {
            return promoCodeRepository.existsByCode(code);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public ResponseDto<AdminCourseAndPromoCodeResponseDto> addPromoCodeToCourse(UUID courseId, AdminCreatePromoCodeRequestDto req) {
        if (promoCodeRepository.existsByCode(req.getCode())) {
            return new ResponseDto<>(false, "Bu promo kod oldindan mavjud");
        }

        Optional<Course> cOp = courseRepository.findAdminByCourseId(req.getCourseId());
        if (cOp.isEmpty()) {
            return ResponseDto.error("Ushbu kurs topilmadi: " + req.getCourseId());
        }
        PromoCode promoCode = new PromoCode();
        promoCode.setCode(req.getCode());
        promoCode.setCourse(cOp.get());
        promoCode.setUser(req.getUser());
        promoCode.setDiscountPercent(req.getDiscountPercent());
        promoCode.setFixedAmount(req.getFixedAmount());
        promoCode.setMaxUsage(req.getMaxUsage());
        promoCode.setUserCount(0);
        promoCode.setValidFrom(req.getValidFrom());
        promoCode.setValidUntil(req.getValidUntil());
        promoCode.setActive(true);
        promoCode.setCreated(CurrentTime.currentTime());
        promoCode.setUpdated(CurrentTime.currentTime());
        promoCode.setPromoCodeUsages(new ArrayList<>());
        return new ResponseDto<>(true, "Success", AdminPromoCodeMapper.toDto(
                promoCodeRepository.save(promoCode), userDeviceService));
    }

    @Override
    public ResponseDto<List<AdminCourseAndPromoCodeResponseDto>> getAllPromoCodes() {
        List<PromoCode> promoCodes = promoCodeRepository.adminGetAllPromoCodes();
        List<AdminCourseAndPromoCodeResponseDto> res = promoCodes.stream().map(promoCode ->
                AdminPromoCodeMapper.toDto(promoCode, userDeviceService)).toList();
        return new ResponseDto<>(true, "Success", res);
    }


    @Override
    public ResponseDto<Page<AdminCourseAndPromoCodeResponseDto>> getAllPromoCodes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PromoCode> promoCodePage = promoCodeRepository.adminGetAllPromoCodes(pageable);
        Page<AdminCourseAndPromoCodeResponseDto> dtoPage = promoCodePage.map(pc -> (AdminPromoCodeMapper.toDto(
                pc, userDeviceService
        )));
        return new ResponseDto<>(true, "Success", dtoPage);
    }


    @Override
    public ResponseDto<List<AdminCourseAndPromoCodeResponseDto>> getAllPromoCodesByCourseId(UUID courseId) {
        if (!courseRepository.existsCourseById(courseId)) {
            return new ResponseDto<>(false, "Not Found Course id: " + courseId);
        }
        List<PromoCode> promoCodes = promoCodeRepository.adminGetAllPromoCodeByCourseId(courseId);
        List<AdminCourseAndPromoCodeResponseDto> res = promoCodes.stream().map(pc -> (AdminPromoCodeMapper.toDto(
                pc, userDeviceService
        ))).toList();
        return new ResponseDto<>(true, "Success", res);
    }

    @Override
    public ResponseDto<Page<AdminCourseAndPromoCodeResponseDto>> getAllPromoCodesByCourseId(UUID courseId, int page, int size) {
        if (!courseRepository.existsCourseById(courseId)) {
            return new ResponseDto<>(false, "Not Found Course id: " + courseId);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<PromoCode> promoCodePage = promoCodeRepository.adminGetAllPromoCodeByCourseId(courseId, pageable);
        Page<AdminCourseAndPromoCodeResponseDto> dtoPage = promoCodePage.map(pc -> (AdminPromoCodeMapper.toDto(
                pc, userDeviceService
        )));
        return new ResponseDto<>(true, "Success", dtoPage);
    }

    @Override
    public ResponseDto<List<AdminCourseAndPromoCodeResponseDto>> getAllPromoCodesByTeacherId(Long teacherId) {
        if (!teacherRepository.existsTeacherById(teacherId)) {
            return new ResponseDto<>(false, "Not Found Teacher id: " + teacherId);
        }
        List<PromoCode> promoCodes = promoCodeRepository.adminGetAllPromoCodeByTeacherId(teacherId);
        List<AdminCourseAndPromoCodeResponseDto> res = promoCodes.stream().map(pc -> (AdminPromoCodeMapper.toDto(
                pc, userDeviceService
        ))).toList();
        return new ResponseDto<>(true, "Success", res);
    }

    @Override
    public ResponseDto<Page<AdminCourseAndPromoCodeResponseDto>> getAllPromoCodesByTeacherId(Long teacherId, int page, int size) {
        if (!teacherRepository.existsTeacherById(teacherId)) {
            return new ResponseDto<>(false, "Not Found Course id: " + teacherId);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<PromoCode> promoCodePage = promoCodeRepository.adminGetAllPromoCodeByTeacherId(teacherId, pageable);
        Page<AdminCourseAndPromoCodeResponseDto> dtoPage = promoCodePage.map(pc -> (AdminPromoCodeMapper.toDto(
                pc, userDeviceService
        )));
        return new ResponseDto<>(true, "Success", dtoPage);
    }
}
