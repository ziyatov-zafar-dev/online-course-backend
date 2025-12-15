package uz.codebyz.onlinecoursebackend.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.codebyz.onlinecoursebackend.message.entity.BlockUser;
import uz.codebyz.onlinecoursebackend.user.User;

import java.util.UUID;

public interface BlockUserRepository extends JpaRepository<BlockUser, UUID> {
    boolean existsByFromAndToAndActiveTrue(User sender, User receiver);
}
