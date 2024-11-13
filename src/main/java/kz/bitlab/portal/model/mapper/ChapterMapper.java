package kz.bitlab.portal.model.mapper;

import kz.bitlab.portal.model.dto.chapterDto.CreateChapterDto;
import kz.bitlab.portal.model.dto.chapterDto.GetChapterDto;
import kz.bitlab.portal.model.entities.Chapter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChapterMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "course", ignore = true)
    Chapter createChapterDtoToEntity(CreateChapterDto createChapterDto);

    GetChapterDto toGetChapterDto(Chapter chapter);

    List<GetChapterDto> toDtoList(List<Chapter> chapters);
}
