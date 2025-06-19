package uz.zafar.onlinecourse.rest.teacher;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zafar.onlinecourse.dto.group_dto.req.AddGroupDto;
import uz.zafar.onlinecourse.dto.group_dto.req.EditGroupDto;
import uz.zafar.onlinecourse.service.GroupService;

import java.util.UUID;

@RequestMapping("/api/teacher/group")
@RestController
@Tag(name = "Teacher Group Controller", description = "Teacher paneli uchun guruhlar bilan ishlash API'lari")
public class TeacherGroupRestController {
    private final GroupService groupService;

    public TeacherGroupRestController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("group-by-id/{groupId}")
    public ResponseEntity<?> groupById(@PathVariable UUID groupId) {
        return ResponseEntity.ok(groupService.getById(groupId));
    }

    @GetMapping("groups/{courseId}")
    public ResponseEntity<?> getAllGroups(
            @PathVariable UUID courseId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ResponseEntity.ok(groupService.getAllByCourseId(page, size, courseId));
    }

    @GetMapping("teacher-groups/{teacherId}")
    public ResponseEntity<?> findAllByTeacherId(
            @PathVariable Long teacherId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ResponseEntity.ok(groupService.findAllByTeacherId(teacherId, page, size));
    }

    @PostMapping("add-group")
    public ResponseEntity<?> addGroup(@RequestBody AddGroupDto group) {
        return ResponseEntity.ok(groupService.addGroup(group));
    }

    @PutMapping("edit-group/{groupId}")
    public ResponseEntity<?> editGroup(@RequestBody EditGroupDto group, @PathVariable UUID groupId) {
        return ResponseEntity.ok(groupService.editGroup(groupId, group));
    }

    @DeleteMapping("delete-group/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable UUID groupId) {
        return ResponseEntity.ok(groupService.deleteGroup(groupId));
    }
}
