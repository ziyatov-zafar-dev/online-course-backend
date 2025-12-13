package uz.codebyz.onlinecoursebackend.payment.entity;

public enum PaymentStatus {
    PENDING("Kutilmoqda", "Pending"),
    SUCCESS("Muvaffaqiyatli", "Success"),
    FAILED("Muvaffaqiyatsiz", "Failed"),
    CANCELLED("Bekor qilindi", "Cancelled"),
    REFUNDED("Qaytarildi", "Refunded");

    private final String uz;
    private final String en;

    PaymentStatus(String uz, String en) {
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