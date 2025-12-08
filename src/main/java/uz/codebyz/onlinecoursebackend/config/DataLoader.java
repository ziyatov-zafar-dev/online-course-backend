package uz.codebyz.onlinecoursebackend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.codebyz.onlinecoursebackend.student.entity.Student;
import uz.codebyz.onlinecoursebackend.student.repository.StudentRepository;
import uz.codebyz.onlinecoursebackend.teacher.entity.Teacher;
import uz.codebyz.onlinecoursebackend.teacher.entity.TeacherStatus;
import uz.codebyz.onlinecoursebackend.teacher.repository.TeacherRepository;
import uz.codebyz.onlinecoursebackend.teacherprice.entity.TeacherPrice;
import uz.codebyz.onlinecoursebackend.teacherprice.repository.TeacherPriceRepository;
import uz.codebyz.onlinecoursebackend.user.User;
import uz.codebyz.onlinecoursebackend.user.UserRepository;
import uz.codebyz.onlinecoursebackend.user.UserRole;
import uz.codebyz.onlinecoursebackend.user.UserStatus;
import uz.codebyz.onlinecoursebackend.userDevice.entity.MaxDevice;
import uz.codebyz.onlinecoursebackend.userDevice.repository.MaxDeviceRepository;

import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

    private static final String ADMIN_EMAIL = "ziyatovzafar98@gmail.com";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "Admin@123";

    private static final String TEACHER_EMAIL = "qayumahad078@gmail.com";
    private static final String TEACHER_USERNAME = "teacher";
    private static final String TEACHER_PASSWORD = "Teacher@123";

    private static final String STUDENT_EMAIL = "zziyatov811@gmail.com";
    private static final String STUDENT_USERNAME = "student";
    private static final String STUDENT_PASSWORD = "Student@123";

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final TeacherPriceRepository teacherPriceRepository;
    private final MaxDeviceRepository maxDeviceRepository;

    public DataLoader(UserRepository userRepository,
                      TeacherRepository teacherRepository,
                      StudentRepository studentRepository,
                      PasswordEncoder passwordEncoder, TeacherPriceRepository teacherPriceRepository, MaxDeviceRepository maxDeviceRepository) {
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.teacherPriceRepository = teacherPriceRepository;
        this.maxDeviceRepository = maxDeviceRepository;
    }

    @Override
    public void run(String... args) {

        if (maxDeviceRepository.findAll().isEmpty()) {
            MaxDevice maxDevice = new MaxDevice();
            maxDevice.setDeviceCount(3);
            maxDeviceRepository.save(maxDevice);
        }
        if (teacherPriceRepository.findAll().isEmpty()) {
            TeacherPrice teacher = new TeacherPrice();
            teacher.setFree(true);
            teacher.setPrice(BigDecimal.valueOf(0));
            teacherPriceRepository.save(teacher);
        }

        // CREATE DEFAULT ADMIN
        if (!userRepository.existsByEmail(ADMIN_EMAIL)) {
            User admin = new User();
            admin.setFirstname("Super");
            admin.setLastname("Admin");
            admin.setEmail(ADMIN_EMAIL);
            admin.setUsername(resolveUsername(ADMIN_USERNAME));
            admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
            admin.setRole(UserRole.ADMIN);
            admin.setStatus(UserStatus.ACTIVE);
            admin.setEnabled(true);
            admin.setAccountNonLocked(true);

            userRepository.save(admin);
        }

        // CREATE DEFAULT TEACHER
        if (!userRepository.existsByEmail(TEACHER_EMAIL)) {

            User userTeacher = new User();
            userTeacher.setFirstname("Default");
            userTeacher.setLastname("Teacher");
            userTeacher.setEmail(TEACHER_EMAIL);
            userTeacher.setUsername(resolveUsername(TEACHER_USERNAME));
            userTeacher.setPassword(passwordEncoder.encode(TEACHER_PASSWORD));
            userTeacher.setRole(UserRole.TEACHER);
            userTeacher.setStatus(UserStatus.ACTIVE);
            userTeacher.setEnabled(true);
            userTeacher.setAccountNonLocked(true);

            User savedUser = userRepository.save(userTeacher);

            Teacher teacher = new Teacher();
            teacher.setUser(savedUser);
            teacher.setStatus(TeacherStatus.OPEN);

            teacherRepository.save(teacher);
        }

        // CREATE DEFAULT STUDENT
        if (!userRepository.existsByEmail(STUDENT_EMAIL)) {

            // 1) User yaratish
            User studentUser = new User();
            studentUser.setFirstname("Default");
            studentUser.setLastname("Student");
            studentUser.setEmail(STUDENT_EMAIL);
            studentUser.setUsername(resolveUsername(STUDENT_USERNAME));
            studentUser.setPassword(passwordEncoder.encode(STUDENT_PASSWORD));
            studentUser.setRole(UserRole.STUDENT);
            studentUser.setStatus(UserStatus.ACTIVE);
            studentUser.setEnabled(true);
            studentUser.setAccountNonLocked(true);

            User savedStudentUser = userRepository.save(studentUser);

            // 2) Student jadvaliga yozish
            Student student = new Student();
            student.setUser(savedStudentUser);

            studentRepository.save(student);
        }
    }

    private String resolveUsername(String baseUsername) {
        String candidate = baseUsername;
        int suffix = 1;
        while (userRepository.existsByUsername(candidate)) {
            candidate = baseUsername + suffix;
            suffix++;
        }
        return candidate;
    }
}
