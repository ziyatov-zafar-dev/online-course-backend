package uz.codebyz.onlinecoursebackend.certificate.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.codebyz.onlinecoursebackend.certificate.ccertificateDto.CertificateResponseDto;
import uz.codebyz.onlinecoursebackend.certificate.ccertificateDto.SkillDto;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.helper.CertificateGenerator;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public interface CertificateService {

    // uploads/certificates

    ResponseDto<CertificateResponseDto> generate(
            String fullName, String courseName,
            CertificateGenerator.CertificateType type,
            UUID certificateId
    );

}
