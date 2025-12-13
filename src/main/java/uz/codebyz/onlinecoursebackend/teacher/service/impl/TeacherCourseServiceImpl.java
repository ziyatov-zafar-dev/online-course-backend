package uz.codebyz.onlinecoursebackend.teacher.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.codebyz.onlinecoursebackend.admin.category.mapper.AdminPromoCodeMapper;
import uz.codebyz.onlinecoursebackend.admin.category.promoCodeDtos.AdminCourseAndPromoCodeResponseDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseResponseDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseSkillResponseDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.AdminCourseUpdateRequestDto;
import uz.codebyz.onlinecoursebackend.admin.course.dto.UploadFileResponseDto;
import uz.codebyz.onlinecoursebackend.admin.course.mapper.AdminCourseMapper;
import uz.codebyz.onlinecoursebackend.admin.course.mapper.AdminCourseSkillMapper;
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
import uz.codebyz.onlinecoursebackend.skill.entity.Skill;
import uz.codebyz.onlinecoursebackend.skill.repository.SkillRepository;
import uz.codebyz.onlinecoursebackend.teacher.dto.course.*;
import uz.codebyz.onlinecoursebackend.teacher.dto.promoCode.TeacherPromoCodeMapper;
import uz.codebyz.onlinecoursebackend.teacher.dto.promoCodeDtos.TeacherCourseAndPromoCodeResponseDto;
import uz.codebyz.onlinecoursebackend.teacher.dto.promoCodeDtos.TeacherCreatePromoCodeRequestDto;
import uz.codebyz.onlinecoursebackend.teacher.entity.Teacher;
import uz.codebyz.onlinecoursebackend.teacher.mapper.TeacherCourseMapper;
import uz.codebyz.onlinecoursebackend.teacher.mapper.TeacherCourseSkillMapper;
import uz.codebyz.onlinecoursebackend.teacher.repository.TeacherRepository;
import uz.codebyz.onlinecoursebackend.teacher.service.TeacherCourseService;
import uz.codebyz.onlinecoursebackend.user.User;
import uz.codebyz.onlinecoursebackend.userDevice.service.UserDeviceService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TeacherCourseServiceImpl implements TeacherCourseService {
    private final CourseRepository courseRepository;
    private final TeacherCourseMapper courseMapper;
    private final CategoryRepository categoryRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final UserDeviceService userDeviceService;
    private final TeacherPromoCodeMapper teacherPromoCodeMapper;
    private final SkillRepository skillRepository;
    private final TeacherCourseSkillMapper teacherCourseSkillMapper;
    private final TeacherRepository teacherRepository;
    @Value("${course.skill.icon}")
    private String skillIconBaseUrl;
    @Value("${course.image.url}")
    private String baseCourseImagePath;
    @Value("${course.video.url}")
    private String baseCourseVideoPath;

    public TeacherCourseServiceImpl(CourseRepository courseRepository, TeacherCourseMapper courseMapper, CategoryRepository categoryRepository, PromoCodeRepository promoCodeRepository, UserDeviceService userDeviceService, TeacherPromoCodeMapper teacherPromoCodeMapper, SkillRepository skillRepository, TeacherCourseSkillMapper teacherCourseSkillMapper, TeacherRepository teacherRepository) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.categoryRepository = categoryRepository;
        this.promoCodeRepository = promoCodeRepository;
        this.userDeviceService = userDeviceService;
        this.teacherPromoCodeMapper = teacherPromoCodeMapper;
        this.skillRepository = skillRepository;
        this.teacherCourseSkillMapper = teacherCourseSkillMapper;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public ResponseDto<List<TeacherCourseResponseDto>> myCourses(Teacher teacher) {
        return new ResponseDto<>(true, "Success", courseMapper.toDto(
                courseRepository.teacherGetMyCourses(teacher.getId())
        ));
    }

    @Override
    public ResponseDto<Page<TeacherCourseResponseDto>> myCourses(Teacher teacher, int page, int size) {
        return new ResponseDto<>(true, "Success", courseMapper.toDto(
                courseRepository.teacherGetMyCourses(teacher.getId(), PageRequest.of(page, size))
        ));

    }

    @Override
    public ResponseDto<TeacherCourseResponseDto> findBySkillId(Teacher teacher, UUID id) {
        Optional<Course> cOp = courseRepository.findTeacherByCourseId(id);
        if (cOp.isEmpty())
            return new ResponseDto<>(false, "Not found course");
        if (!cOp.get().getTeacher().getId().equals(teacher.getId()))
            return new ResponseDto<>(false, "Bu kurs ushbu teacher ga tegishli emas");
        return new ResponseDto<>(true, "Success", courseMapper.toDto(cOp.get()));
    }


    @Override
    public ResponseDto<TeacherCourseResponseDto> findBySlug(String slug) {
        Optional<Course> cOp = courseRepository.findTeacherByCourseSlug(slug);
        return cOp.map(course -> new ResponseDto<>(true, "Success", courseMapper.toDto(course))).orElseGet(() -> new ResponseDto<>(false, "Not found course"));
    }

    @Override
    public ResponseDto<Page<TeacherCourseResponseDto>> findAllByCategoryId(Teacher teacher, UUID categoryId, int page, int size) {
        return new ResponseDto<>(true, "Success", courseMapper.toDto(courseRepository.teacherGetMyCoursesCategoryById(
                teacher.getId(), categoryId, PageRequest.of(page, size)
        )));
    }

    @Override
    public ResponseDto<List<TeacherCourseResponseDto>> findAllByCategoryId(Teacher teacher, UUID categoryId) {
        return new ResponseDto<>(true, "Success", courseMapper.toDto(courseRepository.teacherGetMyCoursesCategoryById(
                teacher.getId(), categoryId
        )));
    }

    @Override
    public ResponseDto<AdminCourseResponseDto> updateCourse(Teacher teacher, UUID courseId, AdminCourseUpdateRequestDto request) {
        Optional<Course> cOp = courseRepository.findAdminByCourseId(courseId);
        if (cOp.isEmpty())
            throw new RuntimeException("Course not found");
        if (!cOp.get().getTeacher().getId().equals(teacher.getId())) {
            return new ResponseDto<>(false, "Bu kurs ushbu teacherga tegishli emas");
        }
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
        ResponseDto<UploadFileResponseDto> checkUploadVideo = FileHelper.uploadFile(video, baseCourseVideoPath, true);
        ResponseDto<UploadFileResponseDto> checkUploadImage = FileHelper.uploadFile(image, baseCourseImagePath, false);
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
    public ResponseDto<Page<TeacherCourseResponseDto>> getByStatus(Teacher teacher, CourseStatus status, int page, int size) {
        Page<Course> adminAllCourses = courseRepository.getTeacherAllByStatus(teacher.getId(),
                status, PageRequest.of(page, size));
        return new ResponseDto<>(true, "Success", courseMapper.toDto(adminAllCourses));
    }

    @Override
    public ResponseDto<List<TeacherCourseResponseDto>> getByStatus(Teacher teacher, CourseStatus status) {
        return new ResponseDto<>(true, "Success", courseMapper.toDto(
                courseRepository.getTeacherAllByStatus(teacher.getId(), status)
        ));
    }

    @Override
    public ResponseDto<TeacherCourseResponseDto> changeCourseStatus(UUID courseId, CourseStatus status) {
        Optional<Course> cOp = courseRepository.findTeacherByCourseId(courseId);
        if (cOp.isEmpty())
            throw new RuntimeException("Course not found id:  " + courseId);
        Course course = cOp.get();
        if (course.getStatus() == status) {
            throw new RuntimeException("Avvalgi status bilan bir xil");
        }
        course.setStatus(status);
        course.setUpdatedAt();
        return new ResponseDto<>(true, "Success", courseMapper.toDto(courseRepository.save(course)));
    }

    @Override
    public ResponseDto<Void> softDelete(UUID courseId) {
        Optional<Course> cOp = courseRepository.findTeacherByCourseId(courseId);
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
        Optional<Course> cOp = courseRepository.findTeacherByCourseId(courseId);
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
    public ResponseDto<TeacherCourseResponseDto> createCourse(TeacherCourseCreateRequestDto request) {

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
        Course course = courseMapper.toEntityFromCreateRequest(request);
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
                courseMapper.toDto(saved)
        );
    }

    @Override
    public ResponseDto<TeacherCourseResponseDto> addVideo(UUID courseId, MultipartFile video) {
        Optional<Course> cOp = courseRepository.findById(courseId);
        if (cOp.isEmpty()) {
            return ResponseDto.error("Bunday ID li kurs mavjud emas. kurs id: " + courseId);
        }
        Course course = cOp.get();

        ResponseDto<UploadFileResponseDto> videoUpload = FileHelper.uploadFile(video, baseCourseVideoPath, true);
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
        return new ResponseDto<>(true, "Success", courseMapper.toDto(courseRepository.save(course)));
    }

    @Override
    public ResponseDto<TeacherCourseResponseDto> addImage(UUID courseId, MultipartFile image) {
        Optional<Course> cOp = courseRepository.findById(courseId);
        if (cOp.isEmpty()) {
            return ResponseDto.error("Bunday ID li kurs mavjud emas. kurs id: " + courseId);
        }
        Course course = cOp.get();
        ResponseDto<UploadFileResponseDto> videoUpload = FileHelper.uploadFile(image, baseCourseImagePath, false);
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
        return new ResponseDto<>(true, "Success", courseMapper.toDto(courseRepository.save(course)));
    }

    @Override
    public ResponseDto<TeacherCourseResponseDto> updateCoursePricing(UUID courseId,
                                                                     TeacherCoursePricingRequestDto request) {

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
        return ResponseDto.ok("Kurs narxi muvaffaqiyatli yangilandi!", courseMapper.toDto(courseRepository.save(course)));
    }

    @Override
    public ResponseDto<TeacherCourseResponseDto> restoreCourse(UUID courseId) {
        Optional<Course> cOp = courseRepository.findTeacherByCourseId(courseId);
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
        return new ResponseDto<>(true, "Ok", courseMapper.toDto(courseRepository.save(course)));
    }

    @Override
    public ResponseDto<TeacherCourseAndPromoCodeResponseDto> findByPromoCodeId(UUID promoCodeId) {
        Optional<PromoCode> pOp = promoCodeRepository.teacherFindById(promoCodeId);
        if (pOp.isEmpty())
            return ResponseDto.error("Not Found PromoCode id: " + promoCodeId);
        PromoCode promoCode = pOp.get();
        TeacherCourseAndPromoCodeResponseDto response = teacherPromoCodeMapper.toDto(promoCode, userDeviceService);
        return new ResponseDto<>(true, "Ok", response);
    }

    @Override
    public ResponseDto<TeacherCourseAndPromoCodeResponseDto> findByPromoCode(String code) {
        Optional<PromoCode> pOp = promoCodeRepository.findByCode(code);
        if (pOp.isEmpty())
            return ResponseDto.error("Not Found code id: " + code);
        PromoCode promoCode = pOp.get();
        TeacherCourseAndPromoCodeResponseDto response = teacherPromoCodeMapper.toDto(promoCode, userDeviceService);
        return new ResponseDto<>(true, "Ok", response);
    }

    @Override
    public boolean existsByPromoCode(String code) {
        return promoCodeRepository.findByCode(code).isPresent();
    }

    @Override
    public ResponseDto<List<TeacherCourseAndPromoCodeResponseDto>> getAllPromoCodes(User user) {
        List<PromoCode> promoCodes = promoCodeRepository.adminGetAllPromoCodes().stream().filter(promoCode -> (
                promoCode.getUser().getId().equals(user.getId())
        )).toList();
        List<TeacherCourseAndPromoCodeResponseDto> res = promoCodes.stream().map(promoCode ->
                teacherPromoCodeMapper.toDto(promoCode, userDeviceService)).toList();
        return new ResponseDto<>(true, "Success", res);
    }

    @Override
    public ResponseDto<Page<TeacherCourseAndPromoCodeResponseDto>> getAllPromoCodes(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PromoCode> promoCodePage = promoCodeRepository.teacherGetAllPromoCodes(user.getId(), pageable);
        Page<TeacherCourseAndPromoCodeResponseDto> dtoPage = promoCodePage.map(pc -> (teacherPromoCodeMapper.toDto(
                pc, userDeviceService
        )));
        return new ResponseDto<>(true, "Success", dtoPage);
    }

    @Override
    public ResponseDto<List<TeacherCourseAndPromoCodeResponseDto>> getAllPromoCodesByCourseId(Teacher teacher, UUID courseId) {
        Optional<Course> cOp = courseRepository.findTeacherByCourseId(courseId);
        if (cOp.isEmpty()) return new ResponseDto<>(false, "Course not found");
        if (!cOp.get().getTeacher().getId().equals(teacher.getId()))
            return new ResponseDto<>(false, "Teacher not found");
        List<PromoCode> promoCodes = promoCodeRepository.adminGetAllPromoCodeByCourseId(courseId);
        List<TeacherCourseAndPromoCodeResponseDto> res = promoCodes.stream().map(pc -> (teacherPromoCodeMapper.toDto(
                pc, userDeviceService
        ))).toList();
        return new ResponseDto<>(true, "Success", res);
    }

    @Override
    public ResponseDto<Page<TeacherCourseAndPromoCodeResponseDto>> getAllPromoCodesByCourseId(Teacher teacher, UUID courseId, int page, int size) {
        Optional<Course> cOp = courseRepository.findTeacherByCourseId(courseId);
        if (cOp.isEmpty()) return new ResponseDto<>(false, "Course not found");
        if (!cOp.get().getTeacher().getId().equals(teacher.getId()))
            return new ResponseDto<>(false, "Teacher not found");
        Pageable pageable = PageRequest.of(page, size);
        Page<PromoCode> promoCodePage = promoCodeRepository.adminGetAllPromoCodeByCourseId(courseId, pageable);
        Page<TeacherCourseAndPromoCodeResponseDto> dtoPage = promoCodePage.map(pc -> (teacherPromoCodeMapper.toDto(
                pc, userDeviceService
        )));
        return new ResponseDto<>(true, "Success", dtoPage);
    }

    @Override
    public ResponseDto<List<TeacherCourseAndPromoCodeResponseDto>> getAllPromoCodesByTeacherId(Teacher teacher) {

        List<PromoCode> promoCodes = promoCodeRepository.adminGetAllPromoCodeByTeacherId(teacher.getId());
        List<TeacherCourseAndPromoCodeResponseDto> res = promoCodes.stream().map(pc -> (teacherPromoCodeMapper.toDto(
                pc, userDeviceService
        ))).toList();
        return new ResponseDto<>(true, "Success", res);
    }

    @Override
    public ResponseDto<Page<TeacherCourseAndPromoCodeResponseDto>> getAllPromoCodesByTeacherId(Long teacherId,
                                                                                               int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PromoCode> promoCodePage = promoCodeRepository.adminGetAllPromoCodeByTeacherId(teacherId, pageable);
        Page<TeacherCourseAndPromoCodeResponseDto> dtoPage = promoCodePage.map(pc -> (teacherPromoCodeMapper.toDto(
                pc, userDeviceService
        )));
        return new ResponseDto<>(true, "Success", dtoPage);
    }

    @Override
    public ResponseDto<TeacherCourseAndPromoCodeResponseDto> addPromoCodeToCourse(UUID courseId, TeacherCreatePromoCodeRequestDto req) {
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
        return new ResponseDto<>(true, "Success", teacherPromoCodeMapper.toDto(
                promoCodeRepository.save(promoCode), userDeviceService));
    }

    @Override
    public ResponseDto<TeacherCourseSkillResponseDto> findBySkillId(UUID skillId) {
        Optional<Skill> sOp = skillRepository.findById(skillId);
        if (sOp.isEmpty()) return new ResponseDto<>(false, "Skill not found");
        Skill skill = sOp.get();
        TeacherCourseSkillResponseDto dto = teacherCourseSkillMapper.toDto(skill);
        return new ResponseDto<>(true, "Success", dto);
    }

    @Override
    public ResponseDto<List<TeacherCourseSkillResponseDto>> findAllByCourseId(UUID courseId) {
        if (courseRepository.findById(courseId).isEmpty()) return new ResponseDto<>(false, "Course not found");
        return new ResponseDto<>(true, "Success",
                teacherCourseSkillMapper.toDto(skillRepository.getAllSkillsByCourse(courseId)));
    }

    @Override
    public ResponseDto<TeacherCourseSkillResponseDto> addSkillToCourse(CreateTeacherCourseSkillDto dto) {
        Optional<Course> cOp = courseRepository.findAdminByCourseId(dto.getCourseId());
        if (cOp.isEmpty()) return new ResponseDto<>(false, "Course not found");
        if (dto.getSkillIcon() == null)
            return new ResponseDto<>(false, "Skill ikonkasi topilmadi");
        MultipartFile image = dto.getSkillIcon();
        String contentType = image.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Faqat rasm yuklash mumkin! (jpeg, png, webp...)");
        }
        ResponseDto<UploadFileResponseDto> savedFile = FileHelper.uploadFile(dto.getSkillIcon(), skillIconBaseUrl, false);
        if (!savedFile.isSuccess()) return new ResponseDto<>(false, savedFile.getMessage());
        Skill skill = new Skill();
        skill.setCourse(cOp.get());
        skill.setName(dto.getName());
        skill.setIconUrl(savedFile.getData().getFileUrl());
        skill.setActive(true);
        skill.setCreated(CurrentTime.currentTime());
        skill.setUpdated(CurrentTime.currentTime());
        skill.setOrderNumber(dto.getOrderNumber());
        skill = skillRepository.save(skill);
        return new ResponseDto<>(true, "Success", teacherCourseSkillMapper.toDto(skill));
    }

    @Override
    public ResponseDto<TeacherCourseSkillResponseDto> editCourseSkill(UUID skillId,
                                                                      UpdateTeacherCourseSkillDto dto) {
        Optional<Skill> sOp = skillRepository.findById(skillId);
        if (sOp.isEmpty()) return new ResponseDto<>(false, "Skill not found");

        if (dto.getSkillIcon() == null)
            return new ResponseDto<>(false, "Skill ikonkasi topilmadi");

        MultipartFile image = dto.getSkillIcon();

        String contentType = image.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Faqat rasm yuklash mumkin! (jpeg, png, webp...)");
        }

        ResponseDto<UploadFileResponseDto> savedFile =
                FileHelper.uploadFile(dto.getSkillIcon(),
                        skillIconBaseUrl, false);
        if (!savedFile.isSuccess()) return new ResponseDto<>(false, savedFile.getMessage());
        Skill skill = sOp.get();
        skill.setName(dto.getName());
        FileHelper.deleteFile(skill.getIconUrl());
        skill.setIconUrl(savedFile.getData().getFileUrl());
        skill.setUpdated(CurrentTime.currentTime());
        skill.setOrderNumber(dto.getOrderNumber());
        skill = skillRepository.save(skill);
        return new ResponseDto<>(true, "Success", teacherCourseSkillMapper.toDto(skill));
    }

    @Override
    public ResponseDto<Void> deleteCourseSkill(UUID skillId) {
        Optional<Skill> sOp = skillRepository.findById(skillId);
        if (sOp.isEmpty()) return new ResponseDto<>(false, "Skill not found");
        Skill skill = sOp.get();
        skill.setActive(false);
        FileHelper.deleteFile(skill.getIconUrl());
        skill.setUpdated(CurrentTime.currentTime());
        skill = skillRepository.save(skill);
        return new ResponseDto<>(true, "Muvaffaqiyatli o'chirildi");
    }
}
