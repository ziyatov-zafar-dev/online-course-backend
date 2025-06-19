package uz.zafar.onlinecourse.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.zafar.onlinecourse.db.domain.TeacherGroup;

import java.util.UUID;

public interface TeacherGroupRepository extends JpaRepository<TeacherGroup, UUID> {
}
