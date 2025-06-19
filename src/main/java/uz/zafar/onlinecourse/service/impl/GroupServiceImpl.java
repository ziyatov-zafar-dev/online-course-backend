package uz.zafar.onlinecourse.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.zafar.onlinecourse.db.domain.*;
import uz.zafar.onlinecourse.db.repository.*;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.course_dto.res.CourseDto;
import uz.zafar.onlinecourse.dto.group_dto.req.AddGroupDto;
import uz.zafar.onlinecourse.dto.group_dto.req.EditGroupDto;
import uz.zafar.onlinecourse.dto.group_dto.res.GroupAndTeachers;
import uz.zafar.onlinecourse.dto.group_dto.res.GroupDto;
import uz.zafar.onlinecourse.dto.group_dto.res.GroupTeachersAndStudents;
import uz.zafar.onlinecourse.dto.teacher_dto.res.TeacherDto;
import uz.zafar.onlinecourse.helper.TimeUtil;
import uz.zafar.onlinecourse.service.CourseService;
import uz.zafar.onlinecourse.service.GroupService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherGroupRepository teacherGroupRepository;
    private final StudentGroupRepository studentGroupRepository;

    @Override
    public ResponseDto<GroupTeachersAndStudents> getStudentAndTeachersOfGroup(UUID groupId) {
        try {
            if (groupRepository.findById(groupId).isEmpty()) {
                throw new Exception("Not found group with id: " + groupId);
            }
            GroupTeachersAndStudents res = new GroupTeachersAndStudents();
            res.setTeachers(groupRepository.getAllByTeachersOfGroup(groupId));
            res.setStudents(groupRepository.getAllByStudentsOfGroup(groupId));
            res.setGroup(getById(groupId).getData());
            return new ResponseDto<>(true, "success", res);
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<GroupAndTeachers> getAllByTeachersFromCourse(UUID groupId) {
        try {
            ResponseDto<GroupDto> check = getById(groupId);
            if (!check.isSuccess()) {
                throw new Exception(check.getMessage());
            }
            List<TeacherDto> teachers = groupRepository.getAllByTeachersOfGroup(groupId);
            GroupAndTeachers groupAndTeachers = new GroupAndTeachers();
            groupAndTeachers.setTeachers(teachers);
            groupAndTeachers.setGroup(check.getData());
            return new ResponseDto<>(true, "Success", groupAndTeachers);
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Page<GroupDto>> getAllByCourseId(int page, int size, UUID courseId) {
        try {
            ResponseDto<CourseDto> checkCourse = courseService.getCourseById(courseId);
            if (!checkCourse.isSuccess()) {
                throw new Exception(checkCourse.getMessage());
            }
            Page<Group> groups = groupRepository.getAllGroupsByCourseId(courseId, PageRequest.of(page, size));
            List<GroupDto> content = new ArrayList<>();
            for (Group group : groups.getContent()) {
                content.add(new GroupDto(
                        group.getId(),
                        group.getName(),
                        group.getDescription(),
                        group.getTelegramChannel(),
                        group.getHasTelegramChannel(),
                        checkCourse.getData(),
                        groupRepository.getGroupOfCreatedDate(group.getId()),
                        groupRepository.getGroupOfUpdatedDate(group.getId())
                ));
            }
            Page<GroupDto> res = new PageImpl<>(content, PageRequest.of(page, size), groups.getTotalElements());
            return new ResponseDto<>(true, "Success", res);
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<GroupDto> getById(UUID pkey) {
        try {
            Optional<Group> gOp = groupRepository.findById(pkey);
            if (gOp.isEmpty())
                throw new Exception("Not found group id: " + pkey);
            Group group = gOp.get();
            return new ResponseDto<>(true, "success", GroupDto
                    .builder()
                    .pkey(pkey)
                    .name(group.getName())
                    .description(group.getDescription())
                    .telegramChannel(group.getTelegramChannel())
                    .hasTelegramChannel(group.getHasTelegramChannel())
                    .course(courseService.getCourseById(group.getCourse().getId()).getData())
                    .updated(groupRepository.getGroupOfUpdatedDate(group.getId()))
                    .created(groupRepository.getGroupOfCreatedDate(group.getId()))
                    .build());
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<GroupDto> addGroup(AddGroupDto dto) {
        try {
            Optional<Course> cOp = courseRepository.findById(dto.getCoursePkey());
            if (cOp.isEmpty())
                throw new Exception("Not found course pkey: " + dto.getCoursePkey());
            Optional<Teacher> tOp = teacherRepository.findById(dto.getTeacherId());
            if (tOp.isEmpty())
                throw new Exception("Not found teacher id: " + dto.getTeacherId());
            Teacher teacher = tOp.get();
            Course course = cOp.get();
            Group group = Group.builder()
                    .name(dto.getName())
                    .description(dto.getDescription())
                    .telegramChannel(dto.getTelegramChannel())
                    .hasTelegramChannel(dto.getHasTelegramChannel())
                    .course(course)
                    .created(TimeUtil.currentTashkentTime())
                    .updated(TimeUtil.currentTashkentTime())
                    .active(true)
                    .lessons(new ArrayList<>())
                    .build();
            Group save = groupRepository.save(group);
            teacherGroupRepository.save(TeacherGroup.builder()
                    .teacherId(teacher.getId())
                    .groupId(save.getId())
                    .created(TimeUtil.currentTashkentTime())
                    .build());
            return new ResponseDto<>(true, "Success", getById(save.getId()).getData());
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> deleteGroup(UUID groupId) {
        try {
            Optional<Group> gOp = groupRepository.findById(groupId);
            if (gOp.isEmpty())
                throw new Exception("Not found group id: " + groupId);
            Group group = gOp.get();
            group.setName(UUID.randomUUID() + group.getName() + groupId);
            groupRepository.save(group);
            return new ResponseDto<>(true, "Success");
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<GroupDto> editGroup(UUID id, EditGroupDto dto) {
        try {
            Optional<Group> gOp = groupRepository.findById(id);
            if (gOp.isEmpty())
                throw new Exception("Not found group id: " + id);
            Group group = gOp.get();
            group.setName(dto.getName());
            group.setDescription(dto.getDescription());
            group.setTelegramChannel(dto.getTelegramChannel());
            group.setHasTelegramChannel(dto.getHasTelegramChannel());
            Group save = groupRepository.save(group);
            return new ResponseDto<>(true, "Success", getById(save.getId()).getData());
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<?> joinGroup(UUID groupId, Long studentId) {
        try {
            Optional<StudentGroup> check = studentGroupRepository.checkStudentGroup(studentId, groupId);
            if (check.isEmpty()) {
                StudentGroup studentGroup = new StudentGroup();
                studentGroup.setGroupId(groupId);
                studentGroup.setStudentId(studentId);
                studentGroup.setActive(true);
                studentGroup.setCreated(TimeUtil.currentTashkentTime());
                StudentGroup save = studentGroupRepository.save(studentGroup);
                return new ResponseDto<>(true, "Joined group", null);
            } else {
                StudentGroup studentGroup = check.get();
                if (studentGroup.getActive()){
                    studentGroup.setActive(true);
                    studentGroup.setCreated(TimeUtil.currentTashkentTime());
                    StudentGroup save = studentGroupRepository.save(studentGroup);
                    return new ResponseDto<>(true, "Joined group", null);
                }else throw new Exception("already joined group");
            }
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<?> leftGroup(UUID groupId, Long studentId) {
        try {
            Optional<StudentGroup> check = studentGroupRepository.checkStudentGroup(studentId, groupId);
            if (check.isEmpty())throw new Exception("Already left group");
            StudentGroup studentGroup = check.get();
            if (!studentGroup.getActive()){
                throw new Exception("Already left group");
            }
            studentGroup.setActive(false);
            StudentGroup save = studentGroupRepository.save(studentGroup);
            return new ResponseDto<>(true, "Left the group", null);
        } catch (Exception e) {
            log.error(e);
            return new ResponseDto<>(false, e.getMessage());
        }
    }
}
