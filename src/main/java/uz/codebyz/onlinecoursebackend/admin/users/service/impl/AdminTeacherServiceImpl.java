package uz.codebyz.onlinecoursebackend.admin.users.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.codebyz.onlinecoursebackend.admin.category.mapper.AdminPromoCodeMapper;
import uz.codebyz.onlinecoursebackend.admin.users.mapper.AdminTeacherMapper;
import uz.codebyz.onlinecoursebackend.admin.users.service.AdminTeacherService;
import uz.codebyz.onlinecoursebackend.admin.users.teacherDto.AdminCreateTeacherRequestDto;
import uz.codebyz.onlinecoursebackend.admin.users.teacherDto.AdminTeacherResponseDto;
import uz.codebyz.onlinecoursebackend.auth.dto.UserResponse;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;
import uz.codebyz.onlinecoursebackend.teacher.entity.Teacher;
import uz.codebyz.onlinecoursebackend.teacher.entity.TeacherStatus;
import uz.codebyz.onlinecoursebackend.teacher.repository.TeacherRepository;
import uz.codebyz.onlinecoursebackend.user.User;
import uz.codebyz.onlinecoursebackend.user.UserProfile;
import uz.codebyz.onlinecoursebackend.user.UserRepository;
import uz.codebyz.onlinecoursebackend.user.UserRole;
import uz.codebyz.onlinecoursebackend.userDevice.service.UserDeviceService;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminTeacherServiceImpl implements AdminTeacherService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final UserDeviceService userDeviceService;

    public AdminTeacherServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository,
                                   TeacherRepository teacherRepository,
                                   UserDeviceService userDeviceService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.userDeviceService = userDeviceService;
    }

    @Override
    public ResponseDto<UserResponse> addTeacher(AdminCreateTeacherRequestDto req) {

        // 1. User yaratamiz
        User user = new User();
        user.setRole(UserRole.TEACHER);
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setFirstname(req.getFirstname());
        user.setLastname(req.getLastname());
        user.setEmail(req.getEmail());
        user.setEnabled(true);

        // 2. UserProfile
        UserProfile profile = new UserProfile();
        profile.setBio(req.getBio());
        profile.setWebsite(req.getWebsite());
        profile.setTelegram(req.getTelegram());
        profile.setGithub(req.getGithub());
        profile.setLinkedin(req.getLinkedin());
        profile.setTwitter(req.getTwitter());
        profile.setFacebook(req.getFacebook());
        profile.setInstagram(req.getInstagram());

        // Bog‘lash
        profile.setUser(user);
        user.setProfile(profile);

        // ❗ 3. AVVAL USER’ni saqlaymiz
        user = userRepository.save(user);

        // 4. Teacher yaratamiz
        Teacher teacher = new Teacher();
        teacher.setUser(user);
        teacher.setStatus(TeacherStatus.OPEN);

        LocalDateTime now = CurrentTime.currentTime();

// 1 oy keyingi vaqt
        LocalDateTime nextMonth = now.plusMonths(1);

//        teacher.setToOpen(nextMonth);

        // 5. Endi teacher’ni saqlaymiz (user_id mavjud!)
        teacher = teacherRepository.save(teacher);

        // 6. Ikki tomonlama bog‘laymiz
        user.setTeacher(teacher);
        user = userRepository.save(user);
        UserResponse res = AdminPromoCodeMapper.mapToUser(user, userDeviceService);
        res.setOnline(userDeviceService.isUserOnline(user.getId()));
        res.setLastOnline(userDeviceService.getLastSeen(user.getId()));
        return ResponseDto.ok("Teacher added successfully", res);
    }

    @Override
    public ResponseDto<AdminTeacherResponseDto> findById(Long teacherId) {
        Optional<Teacher> tOp = teacherRepository.findById(teacherId);
        if (tOp.isEmpty()) {
            return new ResponseDto<>(false, "Teacher not found");
        }
        Teacher teacher = tOp.get();
        return new ResponseDto<>(true, "Ok", new AdminTeacherResponseDto(teacherId, AdminPromoCodeMapper.mapToUser(teacher.getUser(), userDeviceService)));
    }

    @Override
    public ResponseDto<List<AdminTeacherResponseDto>> getAllTeachers() {
        return new ResponseDto<>(true, "Ok", getList(teacherRepository.adminFindAll()));
    }

    @Override
    public ResponseDto<Page<AdminTeacherResponseDto>> getAllTeachers(int page, int size) {
        Page<Teacher> teachers = teacherRepository.adminFindAll(PageRequest.of(page, size));
        return new ResponseDto<>(true, "Success", getPagination(teachers));
    }

    @Override
    public ResponseDto<List<AdminTeacherResponseDto>> getAllTeachersByStatus(TeacherStatus status) {
        List<Teacher> teachers = teacherRepository.getAllTeachersByStatus(status);
        return new ResponseDto<>(true, "Success", getList(teachers));

    }

    @Override
    public ResponseDto<Page<AdminTeacherResponseDto>> getAllTeachersByStatus(TeacherStatus status, int page, int size) {
        return new ResponseDto<>(true, "Success", getPagination(teacherRepository.getAllTeachersByStatus(status, PageRequest.of(page, size))));
    }

    private List<AdminTeacherResponseDto> getList(List<Teacher> teachers) {
        return teachers.stream().map(t -> new AdminTeacherResponseDto(t.getId(),
                AdminPromoCodeMapper.mapToUser(t.getUser(), userDeviceService))).collect(Collectors.toList());
    }

    private Page<AdminTeacherResponseDto> getPagination(Page<Teacher> teachers) {
        return teachers.map(teacher -> AdminTeacherMapper.toDto(teacher, userDeviceService));
    }
}
