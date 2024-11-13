package kz.bitlab.portal.service;

import kz.bitlab.portal.model.dto.chapterDto.CreateChapterDto;
import kz.bitlab.portal.model.dto.chapterDto.GetChapterDto;
import kz.bitlab.portal.model.dto.chapterDto.UpdatedChapterDto;

import java.util.List;

public interface ChapterService {
    void createChapter( CreateChapterDto createChapterDto);
    void updateChapter(Long id, UpdatedChapterDto updatedChapterDto);
    void deleteChapter(Long id);
    GetChapterDto getChapterById(Long id);
    List<GetChapterDto> getAllChapter();
//    List<GetChapterDto> getChaptersByCourseId(Long courseId);
}
