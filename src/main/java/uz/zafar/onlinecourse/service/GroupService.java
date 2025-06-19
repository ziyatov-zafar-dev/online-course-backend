package uz.zafar.onlinecourse.service;

import org.springframework.data.domain.Page;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.group_dto.req.AddGroupDto;
import uz.zafar.onlinecourse.dto.group_dto.req.EditGroupDto;
import uz.zafar.onlinecourse.dto.group_dto.res.GroupAndTeachers;
import uz.zafar.onlinecourse.dto.group_dto.res.GroupDto;
import uz.zafar.onlinecourse.dto.group_dto.res.GroupTeachersAndStudents;

import java.util.UUID;

public interface GroupService {
    public ResponseDto<Page<GroupDto>> getAllByCourseId(int page, int size, UUID courseId);

    public ResponseDto<GroupAndTeachers> getAllByTeachersFromCourse(UUID groupId);

    public ResponseDto<GroupDto> getById(UUID pkey);

    public ResponseDto<GroupDto> addGroup(AddGroupDto groupDto);

    public ResponseDto<Void> deleteGroup(UUID groupId);

    public ResponseDto<GroupDto> editGroup(UUID id, EditGroupDto groupDto);

    ResponseDto<GroupTeachersAndStudents> getStudentAndTeachersOfGroup(UUID groupId);

    ResponseDto<?> joinGroup(UUID groupId, Long studentId);

    ResponseDto<?> leftGroup(UUID groupId, Long studentId);
}
