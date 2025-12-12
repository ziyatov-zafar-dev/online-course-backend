//package uz.codebyz.onlinecoursebackend.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import uz.codebyz.onlinecoursebackend.student.entity.Student;
//import uz.codebyz.onlinecoursebackend.student.repository.StudentRepository;
//import uz.codebyz.onlinecoursebackend.teacher.entity.Teacher;
//import uz.codebyz.onlinecoursebackend.teacher.entity.TeacherStatus;
//import uz.codebyz.onlinecoursebackend.teacher.repository.TeacherRepository;
//import uz.codebyz.onlinecoursebackend.teacherprice.entity.TeacherPrice;
//import uz.codebyz.onlinecoursebackend.teacherprice.repository.TeacherPriceRepository;
//import uz.codebyz.onlinecoursebackend.user.User;
//import uz.codebyz.onlinecoursebackend.user.UserRepository;
//import uz.codebyz.onlinecoursebackend.user.UserRole;
//import uz.codebyz.onlinecoursebackend.user.UserStatus;
//import uz.codebyz.onlinecoursebackend.userDevice.entity.MaxDevice;
//import uz.codebyz.onlinecoursebackend.userDevice.repository.MaxDeviceRepository;
//
//import java.math.BigDecimal;
//
//@Component
//public class DataLoader implements CommandLineRunner {
//
//    private static final String TEACHER_EMAIL = "qayumahad078@gmail.com";
//    private static final String TEACHER_USERNAME = "teacher";
//    private static final String TEACHER_PASSWORD = "Teacher@123";
//
//    private static final String STUDENT_EMAIL = "ziyatovzafar1004@gmail.com";
//    private static final String STUDENT_USERNAME = "student";
//    private static final String STUDENT_PASSWORD = "Student@123";
//
//    private final UserRepository userRepository;
//    private final TeacherRepository teacherRepository;
//    private final StudentRepository studentRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final TeacherPriceRepository teacherPriceRepository;
//    private final MaxDeviceRepository maxDeviceRepository;
//
//    public DataLoader(UserRepository userRepository,
//                      TeacherRepository teacherRepository,
//                      StudentRepository studentRepository,
//                      PasswordEncoder passwordEncoder, TeacherPriceRepository teacherPriceRepository, MaxDeviceRepository maxDeviceRepository) {
//        this.userRepository = userRepository;
//        this.teacherRepository = teacherRepository;
//        this.studentRepository = studentRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.teacherPriceRepository = teacherPriceRepository;
//        this.maxDeviceRepository = maxDeviceRepository;
//    }
//
//    @Value("${login.device.count}")
//    private int maxDeviceCount;
//    @Value("${admin.email}")
//    private String adminEmail;
//    @Value("${admin.username}")
//    private String adminUsername;
//    @Value("${admin.password}")
//    private String adminPassword;
//
//    @Override
//    public void run(String... args) {
//
//        if (maxDeviceRepository.findAll().isEmpty()) {
//            MaxDevice maxDevice = new MaxDevice();
//            maxDevice.setDeviceCount(maxDeviceCount);
//            maxDeviceRepository.save(maxDevice);
//        }
//        if (teacherPriceRepository.findAll().isEmpty()) {
//            TeacherPrice teacher = new TeacherPrice();
//            teacher.setFree(true);
//            teacher.setPrice(BigDecimal.valueOf(0));
//            teacherPriceRepository.save(teacher);
//        }
//
//        // CREATE DEFAULT ADMIN
//        if (!userRepository.existsByEmail(adminEmail)) {
//            User admin = new User();
//            admin.setFirstname("Super");
//            admin.setLastname("Admin");
//            admin.setEmail(adminEmail);
//            admin.setUsername(resolveUsername(adminUsername));
//            admin.setPassword(passwordEncoder.encode(adminPassword));
//            admin.setRole(UserRole.ADMIN);
//            admin.setStatus(UserStatus.ACTIVE);
//            admin.setEnabled(true);
//            admin.setAccountNonLocked(true);
//
//            userRepository.save(admin);
//        }
//
//        // CREATE DEFAULT TEACHER
//        if (!userRepository.existsByEmail(TEACHER_EMAIL)) {
//
//            User userTeacher = new User();
//            userTeacher.setFirstname("Default");
//            userTeacher.setLastname("Teacher");
//            userTeacher.setEmail(TEACHER_EMAIL);
//            userTeacher.setUsername(resolveUsername(TEACHER_USERNAME));
//            userTeacher.setPassword(passwordEncoder.encode(TEACHER_PASSWORD));
//            userTeacher.setRole(UserRole.TEACHER);
//            userTeacher.setStatus(UserStatus.ACTIVE);
//            userTeacher.setEnabled(true);
//            userTeacher.setAccountNonLocked(true);
//
//            User savedUser = userRepository.save(userTeacher);
//
//            Teacher teacher = new Teacher();
//            teacher.setUser(savedUser);
//            teacher.setStatus(TeacherStatus.OPEN);
//
//            teacherRepository.save(teacher);
//        }
//
//        // CREATE DEFAULT STUDENT
//        if (!userRepository.existsByEmail(STUDENT_EMAIL)) {
//
//            // 1) User yaratish
//            User studentUser = new User();
//            studentUser.setFirstname("Default");
//            studentUser.setLastname("Student");
//            studentUser.setEmail(STUDENT_EMAIL);
//            studentUser.setUsername(resolveUsername(STUDENT_USERNAME));
//            studentUser.setPassword(passwordEncoder.encode(STUDENT_PASSWORD));
//            studentUser.setRole(UserRole.STUDENT);
//            studentUser.setStatus(UserStatus.ACTIVE);
//            studentUser.setEnabled(true);
//            studentUser.setAccountNonLocked(true);
//
//            User savedStudentUser = userRepository.save(studentUser);
//
//            // 2) Student jadvaliga yozish
//            Student student = new Student();
//            student.setUser(savedStudentUser);
//
//            studentRepository.save(student);
//        }
//    }
//
//    private String resolveUsername(String baseUsername) {
//        String candidate = baseUsername;
//        int suffix = 1;
//        while (userRepository.existsByUsername(candidate)) {
//            candidate = baseUsername + suffix;
//            suffix++;
//        }
//        return candidate;
//    }
//}
package uz.codebyz.onlinecoursebackend.config;

import org.springframework.beans.factory.annotation.Value;
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
import uz.codebyz.onlinecoursebackend.user.*;
import uz.codebyz.onlinecoursebackend.userDevice.entity.MaxDevice;
import uz.codebyz.onlinecoursebackend.userDevice.repository.MaxDeviceRepository;

import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

    private static final String TEACHER_EMAIL = "qayumahad078@gmail.com";
    private static final String TEACHER_USERNAME = "teacher";
    private static final String TEACHER_PASSWORD = "Teacher@123";

    private static final String STUDENT_EMAIL = "ziyatovzafar1004@gmail.com";
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
                      PasswordEncoder passwordEncoder,
                      TeacherPriceRepository teacherPriceRepository,
                      MaxDeviceRepository maxDeviceRepository) {
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.teacherPriceRepository = teacherPriceRepository;
        this.maxDeviceRepository = maxDeviceRepository;
    }

    @Value("${login.device.count}")
    private int maxDeviceCount;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {

        // ================= DEVICE LIMIT =================
        if (maxDeviceRepository.count() == 0) {
            MaxDevice maxDevice = new MaxDevice();
            maxDevice.setDeviceCount(maxDeviceCount);
            maxDeviceRepository.save(maxDevice);
        }

        // ================= TEACHER PRICE =================
        if (teacherPriceRepository.count() == 0) {
            TeacherPrice teacherPrice = new TeacherPrice();
            teacherPrice.setFree(true);
            teacherPrice.setPrice(BigDecimal.ZERO);
            teacherPriceRepository.save(teacherPrice);
        }

        // ================= ADMIN =================
        if (!userRepository.existsByEmail(adminEmail)) {

            User admin = new User();
            admin.setFirstname("Super");
            admin.setLastname("Admin");
            admin.setEmail(adminEmail);
            admin.setUsername(resolveUsername(adminUsername));
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(UserRole.ADMIN);
            admin.setStatus(UserStatus.ACTIVE);
            admin.setEnabled(true);
            admin.setAccountNonLocked(true);

            // 🔥 PROFILE
            UserProfile profile = new UserProfile();
            profile.setBio("This is a default bio. You can update it later.");
            profile.setWebsite("https://codebyz.online");
            profile.setTelegram("https://t.me/codebyz");
            profile.setGithub("https://github.com/codebyz");
            profile.setLinkedin("https://linkedin.com/company/codebyz");
            profile.setInstagram("https://instagram.com/codebyz");
            profile.setTwitter("https://twitter.com/codebyz");
            profile.setFacebook("https://facebook.com/codebyz");
            profile.setUser(admin);
            admin.setProfile(profile);

            userRepository.save(admin);
        }

        // ================= TEACHER =================
        if (!userRepository.existsByEmail(TEACHER_EMAIL)) {

            User teacherUser = new User();
            teacherUser.setFirstname("Default");
            teacherUser.setLastname("Teacher");
            teacherUser.setEmail(TEACHER_EMAIL);
            teacherUser.setUsername(resolveUsername(TEACHER_USERNAME));
            teacherUser.setPassword(passwordEncoder.encode(TEACHER_PASSWORD));
            teacherUser.setRole(UserRole.TEACHER);
            teacherUser.setStatus(UserStatus.ACTIVE);
            teacherUser.setEnabled(true);
            teacherUser.setAccountNonLocked(true);

            // 🔥 PROFILE
            UserProfile profile = new UserProfile();
            profile.setBio("This is a default bio. You can update it later.");
            profile.setWebsite("https://codebyz_teacher");
            profile.setTelegram("https://t.me/codebyz_teacher");
            profile.setGithub("https://github.com/codebyz_teacher");
            profile.setLinkedin("https://linkedin.com/company/codebyz_teacher");
            profile.setInstagram("https://instagram.com/codebyz_teacher");
            profile.setTwitter("https://twitter.com/codebyz_teacher");
            profile.setFacebook("https://facebook.com/codebyz_teacher");
            profile.setUser(teacherUser);
            teacherUser.setProfile(profile);

            User savedTeacherUser = userRepository.save(teacherUser);

            Teacher teacher = new Teacher();
            teacher.setUser(savedTeacherUser);
            teacher.setStatus(TeacherStatus.OPEN);

            teacherRepository.save(teacher);
        }

        // ================= STUDENT =================
        if (!userRepository.existsByEmail(STUDENT_EMAIL)) {

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

            // 🔥 PROFILE
            UserProfile profile = new UserProfile();
            profile.setWebsite("https://codebyz_student");
            profile.setTelegram("https://t.me/codebyz_student");
            profile.setGithub("https://github.com/codebyz_student");
            profile.setLinkedin("https://linkedin.com/company/codebyz_student");
            profile.setInstagram("https://instagram.com/codebyz_student");
            profile.setTwitter("https://twitter.com/codebyz_student");
            profile.setFacebook("https://facebook.com/codebyz_student");
            profile.setUser(studentUser);
            studentUser.setProfile(profile);

            User savedStudentUser = userRepository.save(studentUser);

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
