package uz.zafar.onlinecourse.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.zafar.onlinecourse.db.domain.*;
import uz.zafar.onlinecourse.db.repository.*;
import uz.zafar.onlinecourse.db.repository.RoleRepository;
import uz.zafar.onlinecourse.helper.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    private Role role(String roleName) {
        Optional<Role> r = roleRepository.findByName(roleName);
        return r.orElse(null);
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findAll().isEmpty()) {
            Role roleAdmin = new Role("ROLE_ADMIN");
            Role roleStudent = new Role("ROLE_STUDENT");
            Role roleTeacher = new Role("ROLE_TEACHER");
            try {
                roleRepository.save(roleAdmin);
                roleRepository.save(roleStudent);
                roleRepository.save(roleTeacher);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        addTypes();
        if (userRepository.findAll().isEmpty()) {
            addAdmin();
        }
        if (teacherRepository.findAll().isEmpty()) {
            addTeacher();
        }
    }

    private void addTypes() {
        if (!typeRepository.findAll().isEmpty()) {
            return;
        }
        for (Type type : typeRepository.findAll()) {
            type.setActive(false);
            type.setName(UUID.randomUUID() + type.getName() + type.getId());
            typeRepository.save(type);
        }
        List<String> list = List.of(
                "pdf", "xlsx", "jpg", "mp4", "png"
        );
        for (String s : list) {
            try {
                typeRepository.save(Type
                        .builder()
                        .name(s)
                        .active(true)
                        .homeworkFiles(new ArrayList<>())
                        .lessonFiles(new ArrayList<>())
                        .build());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void addTeacher() {
        User user = new User();
        user.setFirstname("Zafar");
        user.setLastname("Ziyatov");
        user.setPassword(passwordEncoder.encode("12345"));
        user.setUsername("ziyatov_zafar");
        user.setEmail("ziyatovzafar@gmail.com");
        user.setRoles(List.of(role("ROLE_TEACHER")));
        user.setVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeTime(null);
        
        try {
            Teacher teacher = new Teacher();
            user.setTeacher(teacher);
            user = userRepository.save(user);
            teacher.setUser(user);
            teacherRepository.save(teacher);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void addAdmin() {
        User user = new User();
        user.setId(null);
        user.setFirstname("ADMIN");
        user.setPassword(passwordEncoder.encode("12345"));
        user.setUsername("admin");
        user.setEmail("ziyatovzafar98@gmail.com");
        user.setRoles(List.of(role("ROLE_ADMIN")));
        user.setStudent(null);
        user.setCreated(TimeUtil.currentTashkentTime());
        user.setUpdated(TimeUtil.currentTashkentTime());
        user.setTeacher(null);
        user.setVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeTime(null);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
