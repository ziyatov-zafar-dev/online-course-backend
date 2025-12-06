package uz.codebyz.onlinecoursebackend.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserProfileImageRepository extends JpaRepository<UserProfileImage, UUID> {
}
