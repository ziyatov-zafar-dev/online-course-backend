package uz.zafar.onlinecourse.rest.student;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zafar.onlinecourse.service.GroupService;

import java.util.UUID;

@RequestMapping("/api/student/group")
@RequiredArgsConstructor
@RestController
@Tag(name = "Student Group Controller", description = "Student paneli uchun Grouplar bilan ishlash API'lari")

public class StudentGroupRestController {
    private final GroupService groupService;

    @Operation(summary = "Guruhni olish", description = "Guruhni ID si bo'yicha qidirish")
    @GetMapping("group-by-id/{groupId}")
    public ResponseEntity<?> groupById(@PathVariable UUID groupId) {
        return ResponseEntity.ok(groupService.getById(groupId));
    }

    @GetMapping("groups/{courseId}")
    @Operation(summary = "Guruhlarni olish", description = "Malum bir kursning barcha guruhlari ro'yxatini olish")
    public ResponseEntity<?> getAllGroups(@PathVariable UUID courseId, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(groupService.getAllByCourseId(page, size, courseId));
    }

    @PostMapping("join-group")
    @Operation(summary = "Join group", description = "Guruhga qo'shilish")
    public ResponseEntity<?> joinGroup(@RequestParam UUID groupId, @RequestParam Long studentId) {
        return ResponseEntity.ok(groupService.joinGroup(groupId, studentId));
    }

    @PostMapping("left-group")
    @Operation(summary = "Left group", description = "Guruhdan chiqish")
    public ResponseEntity<?> left_group(@RequestParam UUID groupId, @RequestParam Long studentId) {
        return ResponseEntity.ok(groupService.leftGroup(groupId, studentId));
    }


    @PostMapping("is-join-group")
    @Operation(summary = "is join group", description = "Guruhga qo'shilganmi ?")
    public ResponseEntity<?> isJoinGroup(@RequestParam UUID groupId, @RequestParam Long studentId) {
        return ResponseEntity.ok(groupService.isJoinGroup(groupId, studentId));
    }

    @GetMapping("group-information/{groupId}")
    @Operation(summary = "group information", description = "Guruh haqidagi malumotlar")
    public ResponseEntity<?> groupInformation(@PathVariable UUID groupId) {
        return ResponseEntity.ok(groupService.groupInformation(groupId));
    }

    @GetMapping("my-groups")
    @Operation(summary = "group information", description = "Guruh haqidagi malumotlar")
    public ResponseEntity<?> myGroups(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(groupService.myGroups(page, size));
    }
}
