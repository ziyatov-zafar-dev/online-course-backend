//package uz.codebyz.onlinecoursebackend.email;
//
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailServiceImpl implements EmailService {
//
//    private final JavaMailSender mailSender;
//    private static final String COMPANY_NAME = "CodeByZ";
//    @Value("${spring.mail.username}")
//    private String gmail;
//
//    public EmailServiceImpl(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }
//
//    @Override
//    public void sendEmail(String to, String subject, String text, String code) {
//        MimeMessage mimeMessage = mailSender.createMimeMessage();
//
//        try {
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
//            helper.setTo(to);
//            helper.setSubject(subject);
//
//            String html = buildHtmlTemplate(subject, text, code);
//            helper.setText(html, true);
//
//            mailSender.send(mimeMessage);
//
//        } catch (MessagingException e) {
//            throw new IllegalStateException("Failed to send email", e);
//        }
//    }
//
//    private String buildHtmlTemplate(String title, String content, String c) {
//        int year = java.time.Year.now().getValue();
//        String html = "";
//        html = html.concat("<!DOCTYPE html>");
//        html = html.concat("<html lang='uz'>");
//        html = html.concat("<head>");
//        html = html.concat("<meta charset='UTF-8'>");
//        html = html.concat("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
//        html = html.concat("<title>" + title + "</title>");
//        html = html.concat("<link href='https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700;900&family=JetBrains+Mono:wght@400;600;700&display=swap' rel='stylesheet'>");
//        html = html.concat("<style>");
//        html = html.concat("body{margin:0;padding:0;background:#0d0f17;font-family:'Inter',sans-serif;color:#e2e8f0;text-align:center;}");
//        html = html.concat(".container{max-width:520px;margin:40px auto;background:#11131c;border-radius:18px;");
//        html = html.concat("border:1px solid rgba(0,255,200,0.15);box-shadow:0 0 25px rgba(0,255,200,0.08);padding:0;overflow:hidden;}");
//        html = html.concat(".header{background:linear-gradient(135deg,#00ffbf,#0099ff);padding:30px;}");
//        html = html.concat(".header{text-align:center;}");
//        html = html.concat(".brand{font-size:36px;font-weight:900;color:#0d0f17;font-family:'JetBrains Mono',monospace;text-shadow:0 0 8px rgba(255,255,255,0.6);}");
//        html = html.concat(".content{padding:30px;}");
//        html = html.concat(".title{font-size:22px;font-weight:700;color:#00eaff;margin-bottom:10px;}");
//        html = html.concat(".subtitle{font-size:14px;color:#94a3b8;line-height:1.6;margin-bottom:20px;}");
//        html = html.concat(".code-box{display:inline-block;font-size:34px;font-weight:800;");
//        html = html.concat("font-family:'JetBrains Mono',monospace;letter-spacing:8px;padding:18px 25px;");
//        html = html.concat("color:#00ffbf;border-radius:14px;background:rgba(0,255,200,0.15);");
//        html = html.concat("border:1px solid rgba(0,255,200,0.4);text-shadow:0 0 10px rgba(0,255,200,0.7);margin:25px 0;}");
//        html = html.concat(".message-text{font-size:14px;color:#cbd5e1;line-height:1.6;");
//        html = html.concat("background:rgba(255,255,255,0.03);padding:15px;border-radius:12px;");
//        html = html.concat("border-left:3px solid #0099ff;margin-top:10px;}");
//        html = html.concat(".footer{font-size:12px;color:#64748b;padding:20px;border-top:1px solid rgba(0,255,200,0.15);}");
//        html = html.concat(".footer a{color:#00eaff;text-decoration:none;font-weight:600;}");
//        html = html.concat("</style>");
//        html = html.concat("</head>");
//        html = html.concat("<body>");
//        html = html.concat("<div class='container'>");
//        html = html.concat("<div class='header'>");
//        html = html.concat("<div class='brand'>&lt;CodeByZ/&gt;</div>");
//        html = html.concat("</div>");
//
//        html = html.concat("<div class='content'>");
//        html = html.concat("<div class='title'>" + title + "</div>");
//        html = html.concat("<div class='subtitle'>Tasdiqlash kodi quyida berilgan. Uni hech kimga bermang.</div>");
//        html = html.concat("<div class='code-box'>" + c + "</div>");
//        html = html.concat("<div class='message-text'>" + content + "</div>");
//        html = html.concat("</div>");
//
//        html = html.concat("<div class='footer'>© " + year + " CodeByZ — Barcha huquqlar himoyalangan.<br>");
//        html = html.concat("<a href='mailto:" + gmail + "'>" + gmail + "</a></div>");
//
//        html = html.concat("</div>");
//        html = html.concat("</body></html>");
//
//        return html;
//    }
//}
package uz.codebyz.onlinecoursebackend.email;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.codebyz.onlinecoursebackend.config.AdminChatConfig;
import uz.codebyz.onlinecoursebackend.telegrambot.service.TelegramNotificationService;

import java.io.IOException;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    private final TelegramNotificationService telegramNotificationService;
    private final AdminChatConfig adminChatConfig;
    //    private final TelegramNotificationService telegramNotificationService;
    @Value("${sendgrid.api-key}")
    private String sendGridApiKey;

    @Value("${spring.application.name}")
    private String appName;

    public EmailServiceImpl(TelegramNotificationService telegramNotificationService, AdminChatConfig adminChatConfig) {
        this.telegramNotificationService = telegramNotificationService;
        this.adminChatConfig = adminChatConfig;
    }

//    public EmailServiceImpl(TelegramNotificationService telegramNotificationService) {
//        this.telegramNotificationService = telegramNotificationService;
//    }

    @Override
    public void sendEmail(String to, String subject, String text, String code) {


//        try {
//            for (Long chatId : chatIds) {
//                try {
//                    telegramNotificationService.sendMessage(chatId, subject + ":" + code);
//                } catch (Exception ignored) {
//
//                }
//            }
//        } catch (Exception ignore) {
//
//        }

        String telegramMessage = """
                🔐 %s
                
                📧 Email: %s
                🔢 Tasdiqlash kodi: %s
                
                ⚠️ Kodni hech kimga bermang!
                """.formatted(subject, to, code);

        for (Long chatId : adminChatConfig.getIds()) {
            try {
                telegramNotificationService.sendMessage(chatId, telegramMessage);
            } catch (Exception ignored) {
            }
        }
        String html = buildHtmlTemplate(subject, text, code);

        Email from = new Email("codebyzplatform@gmail.com", "CodeByZ");
        Email receiver = new Email(to);
        Content content = new Content("text/html", html);
        Mail mail = new Mail(from, subject, receiver, content);

        SendGrid sendGrid = new SendGrid(sendGridApiKey);

        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sendGrid.api(request);

        } catch (IOException ex) {
            throw new RuntimeException("Email yuborishda xatolik: " + ex.getMessage());
        }
    }

    private String buildHtmlTemplate(String title, String content, String code) {

        int year = java.time.Year.now().getValue();

        String html = "";
        html = html.concat("<!DOCTYPE html>");
        html = html.concat("<html lang='uz'>");
        html = html.concat("<head>");
        html = html.concat("<meta charset='UTF-8'>");
        html = html.concat("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        html = html.concat("<title>" + title + "</title>");
        html = html.concat("<link href='https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700;900&display=swap' rel='stylesheet'>");
        html = html.concat("<style>");
        html = html.concat("body{margin:0;padding:0;background:#0d0f17;font-family:'Inter',sans-serif;color:#e2e8f0;text-align:center;}");
        html = html.concat(".container{max-width:520px;margin:40px auto;background:#11131c;border-radius:18px;");
        html = html.concat("border:1px solid rgba(0,255,200,0.15);box-shadow:0 0 25px rgba(0,255,200,0.08);padding:0;overflow:hidden;}");
        html = html.concat(".header{background:linear-gradient(135deg,#00ffbf,#0099ff);padding:30px;text-align:center;}");
        html = html.concat(".brand{font-size:36px;font-weight:900;color:#0d0f17;font-family:'JetBrains Mono',monospace;text-shadow:0 0 8px rgba(255,255,255,0.6);}");
        html = html.concat(".content{padding:30px;}");
        html = html.concat(".title{font-size:22px;font-weight:700;color:#00eaff;margin-bottom:10px;}");
        html = html.concat(".subtitle{font-size:14px;color:#94a3b8;margin-bottom:20px;}");
        html = html.concat(".code-box{display:inline-block;font-size:34px;font-weight:800;font-family:'JetBrains Mono',monospace;");
        html = html.concat("letter-spacing:8px;padding:18px 25px;color:#00ffbf;border-radius:14px;");
        html = html.concat("background:rgba(0,255,200,0.15);border:1px solid rgba(0,255,200,0.4);margin:25px 0;}");
        html = html.concat(".message-text{font-size:14px;color:#cbd5e1;margin-top:10px;background:rgba(255,255,255,0.03);padding:15px;border-radius:12px;");
        html = html.concat("border-left:3px solid #0099ff;line-height:1.6;}");
        html = html.concat(".footer{font-size:12px;color:#64748b;padding:20px;border-top:1px solid rgba(0,255,200,0.15);}");
        html = html.concat("</style>");
        html = html.concat("</head><body>");
        html = html.concat("<div class='container'>");
        html = html.concat("<div class='header'><div class='brand'>&lt;CodeByZ/&gt;</div></div>");
        html = html.concat("<div class='content'>");
        html = html.concat("<div class='title'>" + title + "</div>");
        html = html.concat("<div class='subtitle'>Tasdiqlash kodi:</div>");
        html = html.concat("<div class='code-box'>" + code + "</div>");
        html = html.concat("<div class='message-text'>" + content + "</div>");
        html = html.concat("</div>");
        html = html.concat("<div class='footer'>© " + year + " CodeByZ</div>");
        html = html.concat("</div></body></html>");

        return html;
    }

    @Override
    public void sendNotification(String to, String title, String message) {
        String html = notificationTemplate(title, message, "#00A8FF");
        send(to, title, html);
    }

    @Override
    public void sendSystemNotification(String to, String title, String message) {
        String html = notificationTemplate(title, message, "#9b59b6");
        send(to, title, html);
    }

    @Override
    public void sendSuccess(String to, String message) {
        String html = notificationTemplate("Success", message, "#2ecc71");
        send(to, "Success", html);
    }

    @Override
    public void sendError(String to, String message) {
        String html = notificationTemplate("Error", message, "#e74c3c");
        send(to, "Error", html);
    }

    @Override
    public void sendWarning(String to, String message) {
        String html = notificationTemplate("Warning", message, "#f1c40f");
        send(to, "Warning", html);
    }

    private String notificationTemplate(String title, String text, String color) {
        int year = java.time.Year.now().getValue();

        String html = "";
        html = html.concat("<!DOCTYPE html>");
        html = html.concat("<html lang='en'>");
        html = html.concat("<head>");
        html = html.concat("<meta charset='UTF-8'>");
        html = html.concat("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        html = html.concat("<title>" + title + "</title>");
        html = html.concat("<link href='https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;800&family=JetBrains+Mono:wght@600&display=swap' rel='stylesheet'>");

        html = html.concat("<style>");
        html = html.concat("body{margin:0;padding:0;background:#0a0c14;font-family:'Inter',sans-serif;color:#e2e8f0;text-align:center;}");
        html = html.concat(".wrapper{max-width:560px;margin:40px auto;padding:0 16px;}");
        html = html.concat(".card{background:rgba(17,20,30,0.95);border-radius:20px;padding:32px 28px;");
        html = html.concat("border:1px solid rgba(255,255,255,0.08);box-shadow:0 0 25px rgba(0,255,200,0.06);");
        html = html.concat("backdrop-filter:blur(16px);background-image:linear-gradient(135deg,rgba(255,255,255,0.04),rgba(255,255,255,0.01)),");
        html = html.concat("radial-gradient(circle at top left," + color + "33,transparent 70%);}");
        html = html.concat(".title{font-size:26px;font-weight:800;color:" + color + ";margin-bottom:12px;}");
        html = html.concat(".line{height:3px;width:80px;margin:10px auto 28px;background:" + color + ";border-radius:6px;");
        html = html.concat("box-shadow:0 0 10px " + color + "55;}");
        html = html.concat(".msg{font-size:16px;line-height:1.7;color:#cbd5e1;padding:16px;border-radius:14px;");
        html = html.concat("background:rgba(255,255,255,0.03);border-left:4px solid " + color + ";text-align:left;}");
        html = html.concat(".footer{margin-top:30px;font-size:12px;color:#64748b;}");
        html = html.concat("</style>");

        html = html.concat("</head><body>");
        html = html.concat("<div class='wrapper'>");
        html = html.concat("<div class='card'>");
        html = html.concat("<div class='title'>" + title + "</div>");
        html = html.concat("<div class='line'></div>");
        html = html.concat("<div class='msg'>" + text + "</div>");
        html = html.concat("<div class='footer'>© " + year + " CodeByZ — All rights reserved</div>");
        html = html.concat("</div></div>");
        html = html.concat("</body></html>");

        return html;
    }


    private void send(String to, String subject, String html) {
        Email from = new Email("codebyzplatform@gmail.com", "CodeByZ");
        Email receiver = new Email(to);
        Content content = new Content("text/html", html);
        Mail mail = new Mail(from, subject, receiver, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException e) {
            throw new RuntimeException("Email yuborilmadi: " + e.getMessage());
        }
    }


}
