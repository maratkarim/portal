package kz.bitlab.portal.service;

import kz.bitlab.portal.model.dto.courseDto.CreateCourseDto;
import kz.bitlab.portal.model.dto.courseDto.GetCourseDto;
import kz.bitlab.portal.model.dto.courseDto.UpdatedCourseDto;

import java.util.List;

public interface CourseService {
    void createCourse(CreateCourseDto createCourseDto);
    void updateCourse(Long id, UpdatedCourseDto updatedCourseDto);
    void deleteCourse(Long id);
    GetCourseDto getCourseById(Long id);
    List<GetCourseDto> getAllCourse();
}
