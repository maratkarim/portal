package kz.bitlab.portal.model.mapper;

import kz.bitlab.portal.model.dto.courseDto.CreateCourseDto;
import kz.bitlab.portal.model.dto.courseDto.GetCourseDto;
import kz.bitlab.portal.model.entities.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    @Mapping(target = "id", ignore = true)
    Course createCourseDtoToEntity(CreateCourseDto createCourseDto);

    GetCourseDto toGetCourseDto(Course course);
}
