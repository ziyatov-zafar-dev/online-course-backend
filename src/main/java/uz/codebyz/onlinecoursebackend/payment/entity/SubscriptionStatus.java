package uz.codebyz.onlinecoursebackend.payment.entity;

public enum SubscriptionStatus {
    ACTIVE("Faol", "Active"),
    EXPIRED("Muddati tugagan", "Expired"),
    CANCELLED("Bekor qilingan", "Cancelled"),
    PENDING("Kutilmoqda", "Pending");

    private final String uz;
    private final String en;

    SubscriptionStatus(String uz, String en) {
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