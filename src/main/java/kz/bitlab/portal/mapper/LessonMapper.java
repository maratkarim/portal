package kz.bitlab.portal.mapper;

import kz.bitlab.portal.dto.lessonDto.CreateLessonDto;
import kz.bitlab.portal.dto.lessonDto.GetLessonDto;
import kz.bitlab.portal.model.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "chapter", ignore = true)
    Lesson createLessonDtoToEntity(CreateLessonDto createLessonDto);

    GetLessonDto toGetLessonDto(Lesson lesson);
}
