package uz.zafar.onlinecourse.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.zafar.onlinecourse.db.domain.*;
import uz.zafar.onlinecourse.db.repository.CourseRepository;
import uz.zafar.onlinecourse.db.repository.TeacherCourseRepository;
import uz.zafar.onlinecourse.db.repository.TeacherRepository;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.course_dto.req.AddCourseDto;
import uz.zafar.onlinecourse.dto.course_dto.req.EditCourseDto;
import uz.zafar.onlinecourse.dto.course_dto.res.CourseDto;
import uz.zafar.onlinecourse.dto.course_dto.res.CourseDtoResponse;
import uz.zafar.onlinecourse.dto.date.DateDto;
import uz.zafar.onlinecourse.helper.SecurityHelper;
import uz.zafar.onlinecourse.helper.TimeUtil;
import uz.zafar.onlinecourse.service.AuthService;
import uz.zafar.onlinecourse.service.CourseService;
import uz.zafar.onlinecourse.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final TeacherCourseRepository teacherCourseRepository;
    private final TeacherRepository teacherRepository;
    private final AuthService authService;
    private final UserService userService;

    @Override
    public ResponseDto<Page<CourseDto>> getAllGroupByTeacherId(Long teacherId, int page, int size) {
        try {
            return null;
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Page<CourseDto>> getAllDeleteCourses(int page, int size) {
        return getCourses(page, size, true);
    }

    private ResponseDto<Page<CourseDto>> getCourses(int page, int size, boolean isDelete) {
        try {
            Page<Course> allCourses = courseRepository.getCourses(PageRequest.of(page, size), !isDelete);
            List<CourseDto> contents = new ArrayList<>();
            for (Course course : allCourses) {
                Optional<DateDto> cOp = courseRepository.getCourseOfCreatedDate(course.getId());
                Optional<DateDto> uOp = courseRepository.getCourseOfUpdatedDate(course.getId());

                if (cOp.isEmpty()) {
                    log.warn("Missing created date for course: {}", course.getId());
                }

                if (uOp.isEmpty()) {
                    log.warn("Missing updated date for course: {}", course.getId());
                }

                CourseDto courseDto = CourseDto.builder()
                        .pkey(course.getId())
                        .name(isDelete ? course.getDeletedName() : course.getName())
                        .description(course.getDescription())
                        .telegramChannel(course.getTelegramChannel())
                        .hasTelegramChannel(course.getHasTelegramChannel())
                        .createdAt(cOp.orElse(null))
                        .updatedAt(uOp.orElse(null))
                        .build();
                contents.add(courseDto);
            }
            Page<CourseDto> courseDtoPage = new PageImpl<>(contents, PageRequest.of(page, size), allCourses.getTotalElements());
            return new ResponseDto<>(true, "Success", courseDtoPage);
        } catch (Exception e) {
            log.error("Error while getting courses", e);
            return new ResponseDto<>(false, "Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseDto<Page<CourseDto>> getAllCourses(int page, int size) {
        return getCourses(page, size, false);
    }

    @Override
    public ResponseDto<CourseDto> getCourseById(UUID pkey) {
        try {
            Optional<Course> courseOp = courseRepository.findByCourseId(pkey);
            if (courseOp.isEmpty())
                throw new Exception("Course not found id: " + pkey);
            Course course = courseOp.get();
            Optional<DateDto> cOp = courseRepository.getCourseOfCreatedDate(course.getId());
            Optional<DateDto> uOp = courseRepository.getCourseOfUpdatedDate(course.getId());
            if (cOp.isEmpty() || uOp.isEmpty()) {
                throw new Exception("exception get created date or updated date");
            }
            CourseDto courseDto = CourseDto.builder()
                    .pkey(course.getId())
                    .name(course.getName())
                    .description(course.getDescription())
                    .telegramChannel(course.getTelegramChannel())
                    .hasTelegramChannel(course.getHasTelegramChannel())
                    .createdAt(cOp.get())
                    .updatedAt(uOp.get())
                    .build();
            return new ResponseDto<>(true, "Success", courseDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<?> addCourse(AddCourseDto course) {
        try {
            Optional<Course> cOp1 = courseRepository.findByCourseName(course.getName());
            if (cOp1.isPresent()) {
                throw new Exception("Course already exists: " + course.getName());
            }
            User currentUser = SecurityHelper.getCurrentUser();
            boolean success = false;
            if (currentUser == null) {
                throw new Exception("Current user is null");
            }
            for (Role role : currentUser.getRoles()) {
                if (role.getName().equals("ROLE_ADMIN")) {
                    success = true;
                    break;
                }
            }
            if (!success) {
                if (currentUser.getTeacher() == null) {
                    throw new Exception("Teacher not found");
                }
                if (!currentUser.getTeacher().getId().equals(course.getTeacherId())) {
                    throw new Exception("Teacher is not teacher");
                }
            }
            Optional<Teacher> tOp = teacherRepository.findById(course.getTeacherId());
            if (tOp.isEmpty())
                throw new Exception("Teacher not found id: " + course.getTeacherId());
            Teacher teacher = tOp.get();
            Course c = Course.builder()
                    .name(course.getName())
                    .description(course.getDescription())
                    .telegramChannel(course.getTelegramChannel())
                    .hasTelegramChannel(course.getHasTelegramChannel())
                    .active(true)
                    .created(TimeUtil.currentTashkentTime())
                    .updated(TimeUtil.currentTashkentTime())
                    .build();
            Course save = courseRepository.save(c);
            TeacherCourse teacherCourse = new TeacherCourse();
            teacherCourse.setTeacherId(teacher.getId());
            teacherCourse.setCourseId(save.getId());
            teacherCourse.setCreated(TimeUtil.currentTashkentTime());
            teacherCourse.setUpdated(TimeUtil.currentTashkentTime());
            teacherCourse.setActive(true);
            teacherCourseRepository.save(teacherCourse);
            return new ResponseDto<>(true, "Success", getCourseById(save.getId()).getData());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> deleteCourse(UUID pkey) {
        try {
            Optional<Course> cOp = courseRepository.findByCourseId(pkey);
            if (cOp.isEmpty()) {
                throw new Exception("Not found course id: " + pkey);
            }
            Course course = cOp.get();
            course.setActive(false);
            course.setDeletedName(course.getName());
            course.setName(UUID.randomUUID() + course.getName() + course.getId());
            courseRepository.save(course);
            return new ResponseDto<>(true, "Success");
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto<List<CourseDtoResponse>> getTeacherCourses(Long teacherId, boolean isDeleted) {
        try {
            return new ResponseDto<>(true, "Success",
                    isDeleted ? teacherCourseRepository.getTeacherDeletedCourses(teacherId) :
                            teacherCourseRepository.getTeacherCourses(teacherId));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseDto<>(false, e.getMessage());
        }
    }


    @Override
    public ResponseDto<?> editCourse(UUID pkey, EditCourseDto dto) {
        try {
            Optional<Course> cOp = courseRepository.findByCourseId(pkey);
            if (cOp.isEmpty())
                throw new Exception("Not found course id: " + pkey);
            User currentUser = SecurityHelper.getCurrentUser();
            boolean isAdmin = false;
            if (currentUser == null) {
                throw new Exception("Current user is null");
            }
            for (Role role : currentUser.getRoles()) {
                if (role.getName().equals("ROLE_ADMIN")) {
                    isAdmin = true;
                    break;
                }
            }
            if (!isAdmin) {
                if (currentUser.getTeacher() == null) {
                    throw new Exception("Teacher not found");
                }
            }
            Course course = cOp.get();
            course.setName(dto.getName());
            course.setDescription(dto.getDescription());
            course.setTelegramChannel(dto.getTelegramChannel());
            course.setHasTelegramChannel(dto.getHasTelegramChannel());
            course.setUpdated(TimeUtil.currentTashkentTime());
            courseRepository.save(course);
            return new ResponseDto<>(true, "Success", getCourseById(pkey).getData());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseDto<>(false, e.getMessage());
        }
    }


}