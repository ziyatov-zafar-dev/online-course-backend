package uz.codebyz.onlinecoursebackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String uploadsRoot;

    public WebConfig(@Value("${user.profile.images:uploads/user-profile-images}") String profileImageDir) {
        java.nio.file.Path dir = java.nio.file.Paths.get(profileImageDir).toAbsolutePath().normalize();
        java.nio.file.Path root = dir.getParent() != null ? dir.getParent() : dir;
        this.uploadsRoot = root.toString();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = "file:" + uploadsRoot + "/";
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location);
    }
}
