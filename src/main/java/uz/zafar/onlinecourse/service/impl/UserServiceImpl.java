package uz.zafar.onlinecourse.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.zafar.onlinecourse.component.jwt.JwtService;
import uz.zafar.onlinecourse.db.domain.Role;
import uz.zafar.onlinecourse.db.domain.Student;
import uz.zafar.onlinecourse.db.domain.Teacher;
import uz.zafar.onlinecourse.db.domain.User;
import uz.zafar.onlinecourse.db.repository.RoleRepository;
import uz.zafar.onlinecourse.db.repository.StudentRepository;
import uz.zafar.onlinecourse.db.repository.TeacherRepository;
import uz.zafar.onlinecourse.db.repository.UserRepository;
import uz.zafar.onlinecourse.dto.*;
import uz.zafar.onlinecourse.dto.form.ChangePasswordForm;
import uz.zafar.onlinecourse.dto.form.LoginForm;
import uz.zafar.onlinecourse.dto.student_dto.res.StudentDto;
import uz.zafar.onlinecourse.dto.teacher_dto.res.TeacherDto;
import uz.zafar.onlinecourse.dto.user_dto.req.SignUpForm;
import uz.zafar.onlinecourse.dto.user_dto.res.UserDto;
import uz.zafar.onlinecourse.helper.SecurityHelper;
import uz.zafar.onlinecourse.helper.TimeUtil;
import uz.zafar.onlinecourse.service.EmailService;
import uz.zafar.onlinecourse.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UserServiceImpl implements UserService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    @Value("${verified.email.time.milli.second}")
    private Long verifiedMilliSecond;
    private final JwtService jwtService;
    @Value("${jwt.expire.date}")
    private Long jwtExpireDate;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailService emailService;

    public UserServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService, TeacherRepository teacherRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, EmailService emailService, StudentRepository studentRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.studentRepository = studentRepository;
    }


    private final StudentRepository studentRepository;

    @Override
    public ResponseDto<LoginResponseDto> signIn(LoginForm form) throws Exception {
        try {
            form.setUsername(form.getUsername().toLowerCase());
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword()));
            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(form.getUsername());
                return new ResponseDto<>(true, "ok", LoginResponseDto.builder().access_token(token).refresh_token(token).expireDate(jwtExpireDate).build());
            } else {
                throw new Exception("invalid user request..!!");
            }
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage(), null);
        }

    }

    @Override
    public ResponseDto<?> findAll(int page, int size) {
        try {
            Page<User> userPage = userRepository.findAllByActiveOrderById(true, PageRequest.of(page, size));
            List<UserDto> contents = new ArrayList<>();
            for (User user : userPage.getContent()) {
                UserDto dto = new UserDto();
                dto.setRole(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
                dto.setUsername(user.getUsername());
                dto.setLastname(user.getLastname());
                dto.setFirstname(user.getFirstname());
                dto.setEmail(user.getEmail());
                dto.setUserId(user.getId());
                dto.setCreated(user.getCreated());
                dto.setUpdated(user.getUpdated());/*
                dto.setCreated(userRepository.getUserCreatedDate(user.getId()));
                dto.setUpdated(userRepository.getUserUpdatedDate(user.getId()));*/
                contents.add(dto);
            }

            return new ResponseDto<>(true, "Ok", new PageImpl<>(contents, PageRequest.of(page, size), userPage.getTotalElements()));
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<UserResponseDto> currentUserInfo() throws Exception {
        try {
            User currentUser = SecurityHelper.getCurrentUser();
            if (currentUser == null) {
                throw new Exception("Current user is null");
            }
            List<RoleDto> roles = new ArrayList<>();
            List<Role> rr = userRepository.getRolesFromUser(currentUser.getId());
            for (Role role : rr) {
                roles.add(new RoleDto(role.getId(), role.getName()));
            }
            UserResponseDto userResponseDto = new UserResponseDto(currentUser.getFirstname(), currentUser.getLastname(), currentUser.getEmail(), currentUser.getUsername(), roles);
            return new ResponseDto<>(true, "Success", userResponseDto);
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage(), null);
        }
    }

    @Override
    public ResponseDto<Page<TeacherDto>> findAllTeachers(int page, int size) throws Exception {
        try {
            return new ResponseDto<>(true, "Success", teacherRepository.getAllTeachers(PageRequest.of(page, size)));
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage(), null);
        }
    }

    @Override
    public ResponseDto<Page<StudentDto>> findAllStudents(int page, int size) throws Exception {
        try {
            return new ResponseDto<>(true, "Success", studentRepository.findAllStudents(PageRequest.of(page, size)));
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage(), null);
        }
    }

    @Override
    public ResponseDto<UserDto> findUserById(Long id) throws Exception {
        try {
            Optional<User> uOp = userRepository.findById(id);
            if (uOp.isEmpty()) throw new Exception("User not found");
            User user = uOp.get();
            List<String> roles = new ArrayList<>();
            for (Role role : user.getRoles()) {
                roles.add(role.getName());
            }
//            DateDto1 d1 = ;

            return new ResponseDto<>(true, "Success", UserDto.builder().userId(user.getId()).firstname(user.getFirstname()).lastname(user.getLastname()).email(user.getEmail()).username(user.getUsername()).role(roles).created(user.getCreated()).updated(user.getUpdated()).build());
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage(), null);
        }
    }

    @Override
    public ResponseDto<StudentDto> findByStudentId(Long studentId) throws Exception {
        try {
            if (studentRepository.findById(studentId).isEmpty()) {
                throw new Exception("Student not found");
            }
            return new ResponseDto<>(true, "Success", studentRepository.findByStudentId(studentId));
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage(), null);
        }
    }

    @Override
    public ResponseDto<TeacherDto> findByTeacherId(Long id) throws Exception {
        try {
            Optional<Teacher> tOp = teacherRepository.findById(id);
            if (tOp.isEmpty()) throw new Exception("Teacher not found");
            Teacher teacher = tOp.get();
            return new ResponseDto<>(true, "Success", teacherRepository.findTeacherById(teacher.getId()));
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage(), null);
        }

    }

    private Role role(String roleName) {
        Optional<Role> r = roleRepository.findByName(roleName);
        return r.orElse(null);
    }

    @Override
    public ResponseDto<?> signUp(SignUpForm form) {
        try {
            User user = new User();
            user.setUsername(form.getUsername());
            user.setLastname(form.getLastname());
            user.setFirstname(form.getFirstname());
            user.setEmail(form.getEmail());
            user.setPassword(passwordEncoder.encode(form.getPassword()));
//            user.setPhone(form.getPhone());
            user.setRoles(new ArrayList<>());
            user.setCreated(TimeUtil.currentTashkentTime());
            user.setUpdated(TimeUtil.currentTashkentTime());
            user = userRepository.save(user);
            Student student = new Student();
            student.setUser(user);
            studentRepository.save(student);
            user.setStudent(student);
            String code = String.valueOf(new Random().nextInt(9000 - 1) + 1000);
            emailService.sendCode(form.getEmail(), code);
            user.setVerified(false);
            user.setVerificationCodeTime(new Date());
            user.setVerificationCode(code);
            user = userRepository.save(user);
            return new ResponseDto<>(true, "Success", "Sizning emailingizga tasdiqlash kodi yuborildi, Kodning amal qilish muddati 2 daqiqa");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage(), null);
        }
    }

    @Override
    public ResponseDto<Void> verifyCode(String code) {
        try {
            Optional<User> optionalUser = userRepository.findByVerificationCode(code);
            if (optionalUser.isEmpty()) {
                return new ResponseDto<>(false, "Noto‘g‘ri yoki eskirgan kod.");
            }

            User user = optionalUser.get();
            Date now = new Date();

            long diff = now.getTime() - user.getVerificationCodeTime().getTime();
            if (diff > verifiedMilliSecond) {
                user.setEmail(null);
                user.setUsername(null);
                user.setVerified(false);
                user.setVerificationCode(null);
                user.setVerificationCodeTime(null);
                userRepository.save(user);
                return new ResponseDto<>(false, "Tasdiqlash kodi eskirgan. Qayta ro'yxatdan o'ting.");
            }
            Role role = roleRepository.findByName("ROLE_STUDENT").orElseThrow(() -> new RuntimeException("ROLE_STUDENT topilmadi"));
            List<Role> roles = new ArrayList<>();
            roles.add(role);
            user.setRoles(roles);
            user.setCreated(TimeUtil.currentTashkentTime());
            user.setUpdated(TimeUtil.currentTashkentTime());
            user.setVerified(true);
            user.setVerificationCode(null);
            user.setVerificationCodeTime(null);

            userRepository.save(user);
            return new ResponseDto<>(true, "Muvaffaqiyatli tasdiqlandi.");
        } catch (Exception e) {
            log.error("verifyCode() ichida xatolik yuz berdi: ", e); // stack trace ni chiqaradi
            return new ResponseDto<>(false, "Xatolik: " + e.getClass().getSimpleName() + " - " + (e.getMessage() != null ? e.getMessage() : "no message"));
        }
    }

    @Override
    public ResponseDto<TeacherDto> addTeacher(SignUpForm form) {
        try {
            User user = new User();
            user.setUsername(form.getUsername());
            user.setLastname(form.getLastname());
            user.setFirstname(form.getFirstname());
            user.setEmail(form.getEmail());
            user.setPassword(passwordEncoder.encode(form.getPassword()));
//            user.setPhone(form.getPhone());
            List<Role> roles = new ArrayList<>();
            Role role = roleRepository.findByName("ROLE_TEACHER").orElseThrow(() -> new RuntimeException("ROLE_STUDENT topilmadi"));
            roles.add(role);
            user.setRoles(new ArrayList<>(roles));
            user.setCreated(TimeUtil.currentTashkentTime());
            user.setUpdated(TimeUtil.currentTashkentTime());
            user.setVerified(true);
            user.setVerificationCode(null);
            user.setVerificationCodeTime(null);
            user.setVerificationCode(null);
            user = userRepository.save(user);
            Teacher teacher = new Teacher();
            teacher.setUser(user);
            teacher = teacherRepository.save(teacher);
            user.setTeacher(teacher);
            user = userRepository.save(user);
            return new ResponseDto<>(true, "Success", findByTeacherId(teacher.getId()).getData());
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<?> changePassword(ChangePasswordForm form, Long userId) {
        try {
            Optional<User> uOp = userRepository.findById(userId);
            if (uOp.isEmpty()) {
                return new ResponseDto<>(false, "User not found", null);
            }
            User user = uOp.get();
            if (!passwordEncoder.matches(form.getOldPassword(), user.getPassword())) {
                return new ResponseDto<>(false, "Old password is incorrect", null);
            }
            user.setPassword(passwordEncoder.encode(form.getNewPassword()));
            return new ResponseDto<>(true, "Password changed successfully", userRepository.save(user));
        } catch (Exception e) {
            log.error("Error while changing password", e);
            return new ResponseDto<>(false, "Something went wrong", null);
        }
    }


    @Override
    public ResponseDto<UserDto> findByUsername(String username) {
        try {
            Optional<User> uOp = userRepository.findByUsername(username);
            if (uOp.isEmpty()) {
                return new ResponseDto<>(false, "User not found");
            }
            User user = uOp.get();
            return new ResponseDto<>(
                    true, user.isActive() ? "Success" : "User is blocked", mapToUserDto(user)
            );
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<List<UserDto>> searchByUsernameAndFirstNameAndLastnameAndEmail(String query) {
        try {
            List<UserDto> users = new ArrayList<>();
            for (User user : userRepository.searchByUsernameAndFirstNameAndLastnameAndEmail(query)) {
                users.add(mapToUserDto(user));
            }
            return new ResponseDto<>(true, users.isEmpty() ? "Users is empty" : "Success", users);
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }
    private UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(Role::getName).toList(),
                user.getCreated(),
                user.getUpdated()
        );
    }
    @Override
    public ResponseDto<List<UserDto>> searchByUsername(String username) {
        try {
            List<UserDto> users = new ArrayList<>();
            for (User user : userRepository.searchByUsername(username)) {
                users.add(mapToUserDto(user));
            }
            return new ResponseDto<>(true, users.isEmpty() ? "Users is empty" : "Success", users);
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

}
