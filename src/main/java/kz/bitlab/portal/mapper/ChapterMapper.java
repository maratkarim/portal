package kz.bitlab.portal.mapper;

import kz.bitlab.portal.dto.chapterDto.CreateChapterDto;
import kz.bitlab.portal.dto.chapterDto.GetChapterDto;
import kz.bitlab.portal.model.Chapter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChapterMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "course", ignore = true)
    Chapter createChapterDtoToEntity(CreateChapterDto createChapterDto);

    GetChapterDto toGetChapterDto(Chapter chapter);
}
