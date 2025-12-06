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

@Service
public class CertificateService {

    @Value("${course.certificateFolder}")
    private String certificateFolder;  // uploads/certificates
    @Value("${frontend.base-url}")
    private String frontendBaseUrl;  // uploads/certificates

    public ResponseDto<CertificateResponseDto> generate(
            String fullName, String courseName,
            CertificateGenerator.CertificateType type,
            UUID certificateId
    ) {

        try {
            // 1. FAYL EXTENSIYASINI FORMATGA MOS QILAMIZ
            String extension = "." + type.ext();  // pdf, png, jpg ...

            String fileName = fullName.replace(" ", "_")
                    + "_" + System.currentTimeMillis()
                    + extension;

            // 2. PATH NORMALIZE
            Path folderPath = Path.of(certificateFolder)
                    .normalize()
                    .toAbsolutePath();

            // PAPKA YO‘Q BO‘LSA — YARATISH
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            // 3. OUTPUT FILE
            Path output = folderPath.resolve(fileName);

            // 4. SERTIFIKAT YARATISH
            String savedFilePath = CertificateGenerator.generateCertificate(
                    fullName,
                    courseName,
                    CurrentTime.currentTime().toLocalDate(),
                    List.of(
//                            new SkillDto("Java", "uploads/skills/img.png"),
//                            new SkillDto("Spring boot", "uploads/skills/img_1.png"),
//                            new SkillDto("Spring security", "uploads/skills/img_3.png"),
//                            new SkillDto("Postgresql", "uploads/skills/img_2.png"),
//                            new SkillDto("MongoDB", "uploads/skills/img_4.png"),
//                            new SkillDto("HTML", "uploads/skills/img_5.png"),
//                            new SkillDto("CSS", "uploads/skills/img_6.png"),
//                            new SkillDto("ReactJS", "uploads/skills/img_7.png"),
//                            new SkillDto("VueJS", "uploads/skills/img_8.png"),
                            new SkillDto("Antd Design", "uploads/skills/img_9.png"),
                            new SkillDto("Jwt authentication ", "uploads/skills/img_10.png")
                    ),
                    output,
                    frontendBaseUrl + "/certificates",
                    type, certificateId
            );

            // 5. RELATIVE URL → FRONTEND UCHUN
            String relativeUrl =
                    "/" + certificateFolder.replace("\\", "/")
                            + "/" + fileName;

            relativeUrl = relativeUrl.replace("//", "/");

            CertificateResponseDto dto = new CertificateResponseDto(
                    fileName,
                    savedFilePath,   // generateCertificate qaytargan absolute path
                    relativeUrl.substring(1)    // /uploads/certificates/xxx.xxx
            );

            return ResponseDto.ok("Sertifikat muvaffaqiyatli yaratildi!", dto);

        } catch (Exception e) {
            return ResponseDto.error("Xatolik: " + e.getMessage());
        }
    }

}
