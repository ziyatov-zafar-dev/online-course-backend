package uz.zafar.onlinecourse.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.service.EmailService;

import java.io.File;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String email;

    @Override
    public ResponseDto<Void> sendFile(String to, String subject, File file) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true); // true = multipart

            helper.setFrom(email);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(".");

            // Faylni emailga biriktirish
            FileSystemResource fileResource = new FileSystemResource(file);
            helper.addAttachment(file.getName(), fileResource);

            mailSender.send(mimeMessage);

            return new ResponseDto<>(true, "Success");
        } catch (Exception e) {
            log.error("Email yuborishda xatolik: ", e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(email);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            return new ResponseDto<>(true, "Success");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    public ResponseDto<Void> sendCode(String toEmail, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("ONLINE COURSE PLATFORM");

            // HTML formatdagi email matni
            String htmlMsg = "<div style='font-family:Arial,sans-serif;padding:20px;'>"
                    + "<h3>Salom!</h3>"
                    + "<p>Sizning tasdiqlash kodingiz quyidagicha:</p>"
                    + "<div style='margin:10px 0;padding:15px;border:2px dashed #4CAF50;"
                    + "background-color:#f1f1f1;font-size:24px;font-weight:bold;color:#333;'>"
                    + code
                    + "</div>"
                    + "<p>Iltimos, bu kodni 2 daqiqa ichida kiriting.</p>"
                    + "</div>";

            helper.setText(htmlMsg, true); // true -> HTML body

            mailSender.send(message);
            return new ResponseDto<>(true, "Success");
        } catch (MessagingException e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }
}
