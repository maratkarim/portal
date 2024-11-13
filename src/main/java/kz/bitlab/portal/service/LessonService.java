package kz.bitlab.portal.service;

import kz.bitlab.portal.model.dto.courseDto.CreateCourseDto;
import kz.bitlab.portal.model.dto.lessonDto.CreateLessonDto;
import kz.bitlab.portal.model.dto.lessonDto.GetLessonDto;
import kz.bitlab.portal.model.dto.lessonDto.UpdatedLessonDto;

import java.util.List;

public interface LessonService {
    void createLesson(CreateLessonDto createLessonDto);
    void updateLesson(Long id, UpdatedLessonDto updatedLessonDto);
    void deleteLesson(Long id);
    GetLessonDto getLessonById(Long id);
    List<GetLessonDto> getAllLessons();
}
