package uz.zafar.onlinecourse.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.zafar.onlinecourse.db.domain.StudentGroup;

import java.util.Optional;
import java.util.UUID;

public interface StudentGroupRepository extends JpaRepository<StudentGroup, UUID> {
    @Query("""
            SELECT sg
            FROM  StudentGroup sg
            WHERE sg.groupId=:groupId
            AND sg.studentId=:studentId
            AND sg.active=true""")
    Optional<StudentGroup> checkStudentGroup(@Param("studentId") Long studentId, @Param("groupId") UUID groupId);

}
