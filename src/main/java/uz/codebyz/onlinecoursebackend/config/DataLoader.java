package uz.codebyz.onlinecoursebackend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.codebyz.onlinecoursebackend.teacher.entity.Teacher;
import uz.codebyz.onlinecoursebackend.teacher.entity.TeacherStatus;
import uz.codebyz.onlinecoursebackend.teacher.repository.TeacherRepository;
import uz.codebyz.onlinecoursebackend.user.User;
import uz.codebyz.onlinecoursebackend.user.UserRepository;
import uz.codebyz.onlinecoursebackend.user.UserRole;
import uz.codebyz.onlinecoursebackend.user.UserStatus;

@Component
public class DataLoader implements CommandLineRunner {

    private static final String ADMIN_EMAIL = "ziyatovzafar98@gmail.com";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "Admin@123";

    private static final String TEACHER_EMAIL = "qayumahad078@gmail.com";
    private static final String TEACHER_USERNAME = "teacher";
    private static final String TEACHER_PASSWORD = "Teacher@123";

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository,
                      TeacherRepository teacherRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

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

            // 1) User qo‘shish
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

            // 2) Teacher jadvaliga yozish
            Teacher teacher = new Teacher();
            teacher.setUser(savedUser);
            teacher.setStatus(TeacherStatus.OPEN);

            teacherRepository.save(teacher);
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
