package uz.codebyz.onlinecoursebackend.payment.entity;

public enum SubscriptionPeriod {
    MONTHLY(1, "1 oylik", "1 month"),
    QUARTERLY(3, "3 oylik", "3 months"),
    SEMI_ANNUAL(6, "6 oylik", "6 months"),
    ANNUAL(12, "1 yillik", "1 year"),
    BIENNIAL(24, "2 yillik", "2 years"),
    TRIENNIAL(36, "3 yillik", "3 years");

    private final int months;
    private final String uz;
    private final String en;

    SubscriptionPeriod(int months, String uz, String en) {
        this.months = months;
        this.uz = uz;
        this.en = en;
    }

    public int getMonths() {
        return months;
    }

    public String getUz() {
        return uz;
    }

    public String getEn() {
        return en;
    }
}