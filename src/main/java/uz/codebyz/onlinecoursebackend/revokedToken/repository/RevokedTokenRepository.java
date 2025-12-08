package uz.codebyz.onlinecoursebackend.revokedToken.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.codebyz.onlinecoursebackend.revokedToken.entity.RevokedToken;

import java.util.UUID;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken, String> {
}
