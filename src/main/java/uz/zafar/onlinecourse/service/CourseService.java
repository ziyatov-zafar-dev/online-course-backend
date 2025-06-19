package uz.zafar.onlinecourse.service;

import org.springframework.data.domain.Page;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.course_dto.req.AddCourseDto;
import uz.zafar.onlinecourse.dto.course_dto.req.EditCourseDto;
import uz.zafar.onlinecourse.dto.course_dto.res.CourseDto;
import uz.zafar.onlinecourse.dto.course_dto.res.CourseDtoResponse;

import java.util.List;
import java.util.UUID;

public interface CourseService {
    ResponseDto<Page<CourseDto>> getAllCourses(int page, int size);

    ResponseDto<Page<CourseDto>> getAllDeleteCourses(int page, int size);

    ResponseDto<Page<CourseDto>> getAllGroupByTeacherId(Long teacherId, int page, int size);

    ResponseDto<CourseDto> getCourseById(UUID pkey);

    ResponseDto<?> addCourse(AddCourseDto course);

    ResponseDto<Void> deleteCourse(UUID pkey);

    ResponseDto<?> editCourse(UUID pkey, EditCourseDto course);

    ResponseDto<List<CourseDtoResponse>> getTeacherCourses(Long teacherId,boolean isDeleted);
}
