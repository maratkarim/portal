package kz.bitlab.portal.mapper;

import kz.bitlab.portal.dto.courseDto.CreateCourseDto;
import kz.bitlab.portal.dto.courseDto.GetCourseDto;
import kz.bitlab.portal.model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    Course createCourseDtoToEntity(CreateCourseDto createCourseDto);

    GetCourseDto toGetCourseDto(Course course);
}
