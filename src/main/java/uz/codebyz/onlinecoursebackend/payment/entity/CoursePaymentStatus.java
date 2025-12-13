package uz.codebyz.onlinecoursebackend.payment.entity;

public enum CoursePaymentStatus {
    PAID("To'langan", "Paid"),
    REFUNDED("Qaytarilgan", "Refunded"),
    PENDING("Kutilmoqda", "Pending");

    private final String uz;
    private final String en;

    CoursePaymentStatus(String uz, String en) {
        this.uz = uz;
        this.en = en;
    }

    public String getUz() {
        return uz;
    }

    public String getEn() {
        return en;
    }
}