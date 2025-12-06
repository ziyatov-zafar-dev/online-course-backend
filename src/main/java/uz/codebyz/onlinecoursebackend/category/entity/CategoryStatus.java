package uz.codebyz.onlinecoursebackend.category.entity;

public enum CategoryStatus {

    OPEN("Kategoriya ochiq va faol holatda"),
    CLOSE("Kategoriya yopiq. foydalanuvchilarga ko'rinmaydi");

    private final String description;

    CategoryStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
