package uz.codebyz.onlinecoursebackend.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.codebyz.onlinecoursebackend.auth.dto.UserResponse;
import uz.codebyz.onlinecoursebackend.auth.service.AuthService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ProfileImageService {

    private final UserProfileImageRepository imageRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final Path rootDir;
    private final String publicPrefix;

    public ProfileImageService(UserProfileImageRepository imageRepository,
                               UserRepository userRepository,
                               AuthService authService,
                               @Value("${user.profile.images:uploads/user-profile-images}") String dir) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.authService = authService;
        this.rootDir = Paths.get(dir).toAbsolutePath().normalize();
        this.publicPrefix = "/uploads/user-profile-images/";
        initDir();
    }

    private void initDir() {
        try {
            Files.createDirectories(rootDir);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create image directory", e);
        }
    }

    @Transactional
    public UserResponse upload(MultipartFile file, User user) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Fayl bo'sh.");
        }
        User managed = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Foydalanuvchi topilmadi"));
        String ext = extractExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);
        Path target = rootDir.resolve(filename);
        try {
            Files.createDirectories(target.getParent());
            Files.copy(file.getInputStream(), target);
        } catch (IOException e) {
            throw new IllegalStateException("Faylni saqlashda xatolik", e);
        }

        if (managed.getProfile() == null) {
            UserProfile profile = new UserProfile();
            profile.setUser(managed);
            managed.setProfile(profile);
        }

        UserProfileImage image = new UserProfileImage();
        image.setProfile(managed.getProfile());
        image.setUrl(publicPrefix + filename);
        image.setOriginalName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        managed.getProfile().getImages().add(image);

        userRepository.save(managed);
        return authService.getMe(managed).getData();
    }

    private String extractExtension(String name) {
        if (name == null) return "";
        int idx = name.lastIndexOf('.');
        if (idx == -1 || idx == name.length() - 1) {
            return "";
        }
        return name.substring(idx + 1);
    }
}
