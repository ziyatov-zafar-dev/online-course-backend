package uz.codebyz.onlinecoursebackend.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.codebyz.onlinecoursebackend.message.entity.MessageFile;

import java.util.UUID;

public interface MessageFileRepository extends JpaRepository<MessageFile, UUID> {
}
