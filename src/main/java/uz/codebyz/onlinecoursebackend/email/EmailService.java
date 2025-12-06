package uz.codebyz.onlinecoursebackend.email;

public interface EmailService {
    void sendEmail(String to, String subject, String text, String code);

    void sendNotification(String to, String title, String message);

    // 🔔 System notification (Admin uchun)
    void sendSystemNotification(String to, String title, String message);

    // 🔔 Success notification
    void sendSuccess(String to, String message);

    // 🔔 Error notification
    void sendError(String to, String message);

    // 🔔 Warning notification
    void sendWarning(String to, String message);
}
