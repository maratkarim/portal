package kz.bitlab.portal.model.mapper;

import kz.bitlab.portal.model.dto.lessonDto.CreateLessonDto;
import kz.bitlab.portal.model.dto.lessonDto.GetLessonDto;
import kz.bitlab.portal.model.entities.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "chapter", ignore = true)
    Lesson createLessonDtoToEntity(CreateLessonDto createLessonDto);

    GetLessonDto toGetLessonDto(Lesson lesson);

    List<GetLessonDto> toDtoList(List<Lesson> lessons);
}
