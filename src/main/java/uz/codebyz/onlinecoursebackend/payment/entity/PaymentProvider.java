package uz.codebyz.onlinecoursebackend.payment.entity;

public enum PaymentProvider {
    PAYME("Payme", "Payme"),
    CLICK("Click", "Click"),
    UZUM("Uzum Bank", "Uzum Bank"),
    PAYLOV("Paylov", "Paylov"),
    HAMKOR("Hamkor bank", "Hamkor bank"),
    PAYPAL("PayPal", "PayPal"),
    OTHER("Boshqa", "Other");

    private final String uz;
    private final String en;

    PaymentProvider(String uz, String en) {
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