package uz.zafar.onlinecourse.rest.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.group_dto.req.AddGroupDto;
import uz.zafar.onlinecourse.dto.group_dto.req.EditGroupDto;
import uz.zafar.onlinecourse.service.GroupService;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/group/")
@RequiredArgsConstructor
@Tag(name = "Admin group Controller", description = "Admin paneli uchun guruhlar bilan ishlash API'lari")
public class AdminGroupRestController {
    private final GroupService groupService;

    @GetMapping("get-all-groups-by-course-id/{courseId}")
    public ResponseEntity<?> getAllGroupsByCourseId(@PathVariable UUID courseId, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(groupService.getAllByCourseId(page, size, courseId));
    }
    @GetMapping("get-all-teachers-from-course/{groupId}")
    public ResponseEntity<?> getAllByTeachersFromCourse(@PathVariable UUID groupId) {
        return ResponseEntity.ok(groupService.getAllByTeachersFromCourse(groupId));
    }
    @GetMapping("get-group-by-id/{groupId}")
    public ResponseEntity<?> getById(@PathVariable UUID groupId) {
        return ResponseEntity.ok(groupService.getById(groupId));
    }
    @PostMapping("add-group")
    public ResponseEntity<?> addGroup(@RequestBody AddGroupDto group) {
        return ResponseEntity.ok(groupService.addGroup(group));
    }
    @DeleteMapping("delete-group/{groupId}")
    public ResponseEntity<ResponseDto<Void>> deleteGroup(@PathVariable UUID groupId) {
        return ResponseEntity.ok(groupService.deleteGroup(groupId));
    }
    @PutMapping("edit-group/{groupId}")
    public ResponseEntity<ResponseDto<?>> editGroup(@PathVariable UUID groupId, @RequestBody EditGroupDto group) {
        return ResponseEntity.ok(groupService.editGroup(groupId,group));
    }
    @GetMapping("get-students-and-teachers-of-group/{groupId}")
    public ResponseEntity<ResponseDto<?>> getStudentAndTeachersOfGroup(@PathVariable UUID groupId) {
        return ResponseEntity.ok(groupService.getStudentAndTeachersOfGroup(groupId));
    }
}
