package uz.codebyz.onlinecoursebackend;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uz.codebyz.onlinecoursebackend.admin.course.dto.UploadFileResponseDto;
import uz.codebyz.onlinecoursebackend.certificate.service.CertificateService;
import uz.codebyz.onlinecoursebackend.common.ResponseDto;
import uz.codebyz.onlinecoursebackend.email.EmailService;
import uz.codebyz.onlinecoursebackend.helper.CertificateGenerator;
import uz.codebyz.onlinecoursebackend.helper.CurrentTime;
import uz.codebyz.onlinecoursebackend.helper.FileHelper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@RestController
@RequestMapping("/api/auth")
@Hidden
public class ABCDEFGHIJKLMNOPQRSTUVXYZ {
    private final String myEmail = "ziyatovzafar98@gmail.com";
    private final EmailService emailService;
    private final CertificateService certificateService;

    @PostMapping("get-certificate")
    public ResponseDto<?> getCertificate(
            @RequestParam("fullname") String fullname,
            @RequestParam("coursename") String coursename,
            @RequestParam("filetype") CertificateGenerator.CertificateType filetype
    ) {
        return certificateService.generate(
                fullname, coursename, filetype, UUID.randomUUID()
        );
    }

    private ResponseDto<UploadFileResponseDto> uploadFile(MultipartFile file, String folder) {
        try {
            file = FileHelper.compressVideoAndReturn(file);
            if (file.isEmpty()) {
                return ResponseDto.error("Fayl topilmadi!");
            }
            if (!new File(folder).exists()) {
                new File(folder).mkdirs();
            }
            String originalName = file.getOriginalFilename();
            String newFileName = (CurrentTime.currentTime() + "_" + originalName).replaceAll("[^a-zA-Z0-9_.]", "_");
            Path filePath = Paths.get(folder + newFileName);
            Files.write(filePath, file.getBytes());
            return new ResponseDto<>(true, "Ok", new UploadFileResponseDto(
                    originalName,
                    folder + newFileName,
                    file.getSize()
            ));
        } catch (Exception e) {
            return new ResponseDto<>(false, "Kutilmagan xatolik" + e.getMessage());
        }
    }

    @PostMapping(
            value = "file-upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> upload(
            @RequestParam("video") MultipartFile video
    ) {
        ResponseDto<UploadFileResponseDto> savedFile = uploadFile(video, "uploads/helper/");

//        Map<String, Object> result = new HashMap<>();
//        result.put("firstFileSizeBytes", video.getSize());
//        result.put("firstFileSizeMB", String.format("%.2f MB", video.getSize() / 1024.0 / 1024.0));
//
//        result.put("compressedFileSizeBytes", savedFile.getData().getFileSize());
//        result.put("compressedFileSizeMB", String.format("%.2f MB", (savedFile.getData().getFileSize()) / 1024.0 / 1024.0));
//
        return ResponseEntity.ok(savedFile);
    }

    public ABCDEFGHIJKLMNOPQRSTUVXYZ(EmailService emailService, CertificateService certificateService) {
        this.emailService = emailService;
        this.certificateService = certificateService;
    }

    @PostMapping("sendNotification")
    public String sendNotification(@RequestParam("title") String title, @RequestParam("message") String message) {
        emailService.sendNotification(myEmail, title, message);
        return "Muvaffaqiyatli yuborildi";
    }

    @PostMapping("sendSystemNotification")
    public String sendSystemNotification(@RequestParam(name = "title") String title, @RequestParam("message") String message) {
        emailService.sendSystemNotification(myEmail, title, message);
        return "Muvaffaqiyatli yuborildi";
    }

    @PostMapping("sendSuccess")
    public String sendSuccess(@RequestParam("message") String message) {
        emailService.sendSuccess(myEmail, message);
        return "Muvaffaqiyatli yuborildi";
    }

    @PostMapping("sendError")
    public String sendError(@RequestParam("message") String message) {
        emailService.sendError(myEmail, message);
        return "Muvaffaqiyatli yuborildi";
    }

    @PostMapping("sendWarning")
    public String sendWarning(@RequestParam("message") String message) {
        emailService.sendWarning(myEmail, message);
        return "Muvaffaqiyatli yuborildi";
    }



}
