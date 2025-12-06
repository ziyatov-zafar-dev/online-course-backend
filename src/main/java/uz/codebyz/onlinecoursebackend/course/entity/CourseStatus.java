package uz.codebyz.onlinecoursebackend.course.entity;

public enum CourseStatus {

    OPEN("Kurs ochiq va foydalanuvchilar uchun ko‘rinadi"),
    DRAFT("Kurs hali tayyor emas, kamchiliklarni to'g'irlash mumkin"),
    CLOSE("Kurs yopilgan va endi ko‘rinmaydi. Ammo admin ochib berishi mumkin");

    private final String description;

    CourseStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
