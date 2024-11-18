package kz.bitlab.portal.service;

import kz.bitlab.portal.model.dto.lessonDto.CreateLessonDto;
import kz.bitlab.portal.model.dto.lessonDto.GetLessonDto;
import kz.bitlab.portal.model.dto.lessonDto.UpdatedLessonDto;
import kz.bitlab.portal.model.entities.Chapter;
import kz.bitlab.portal.model.entities.Lesson;
import kz.bitlab.portal.model.mapper.LessonMapper;
import kz.bitlab.portal.repository.ChapterRepository;
import kz.bitlab.portal.repository.LessonRepository;
import kz.bitlab.portal.service.impl.LessonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LessonServiceTest {

    @InjectMocks
    private LessonServiceImpl lessonService;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private LessonMapper lessonMapper;

    @Mock
    private ChapterRepository chapterRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateLesson() {
        // Given
        CreateLessonDto createLessonDto = new CreateLessonDto();
        createLessonDto.setChapterId(1L);
        createLessonDto.setName("New Lesson");

        Chapter chapter = new Chapter();
        chapter.setId(1L);

        Lesson lesson = new Lesson();
        lesson.setChapter(chapter);

        // When
        when(chapterRepository.findById(createLessonDto.getChapterId())).thenReturn(java.util.Optional.of(chapter));
        when(lessonMapper.createLessonDtoToEntity(createLessonDto)).thenReturn(lesson);

        lessonService.createLesson(createLessonDto);

        // Then
        verify(lessonRepository, times(1)).save(lesson);
    }

    @Test
    void testUpdateLesson() {
        // Given
        Long lessonId = 1L;
        UpdatedLessonDto updatedLessonDto = new UpdatedLessonDto();
        updatedLessonDto.setName("Updated Lesson");

        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        lesson.setName("Old Lesson");
        lesson.setCreatedTime(LocalDateTime.now());

        // When
        when(lessonRepository.findById(lessonId)).thenReturn(java.util.Optional.of(lesson));

        lessonService.updateLesson(lessonId, updatedLessonDto);

        // Then
        assertEquals("Updated Lesson", lesson.getName());
        verify(lessonRepository, times(1)).save(lesson);
    }

    @Test
    void testDeleteLesson() {
        // Given
        Long lessonId = 1L;
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);

        // When
        when(lessonRepository.findById(lessonId)).thenReturn(java.util.Optional.of(lesson));

        lessonService.deleteLesson(lessonId);

        // Then
        verify(lessonRepository, times(1)).delete(lesson);
    }

    @Test
    void testGetLessonById() {
        // Given
        Long lessonId = 1L;
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);

        GetLessonDto lessonDto = new GetLessonDto();
        lessonDto.setId(lessonId);

        // When
        when(lessonRepository.findById(lessonId)).thenReturn(java.util.Optional.of(lesson));
        when(lessonMapper.toGetLessonDto(lesson)).thenReturn(lessonDto);

        GetLessonDto result = lessonService.getLessonById(lessonId);

        // Then
        assertEquals(lessonId, result.getId());
        verify(lessonRepository, times(1)).findById(lessonId);
    }

    @Test
    void testGetAllLessons() {
        // Given
        List<Lesson> lessons = new ArrayList<>();
        Lesson lesson1 = new Lesson();
        Lesson lesson2 = new Lesson();
        lessons.add(lesson1);
        lessons.add(lesson2);

        GetLessonDto lessonDto1 = new GetLessonDto();
        GetLessonDto lessonDto2 = new GetLessonDto();

        // When
        when(lessonRepository.findAll()).thenReturn(lessons);
        when(lessonMapper.toGetLessonDto(lesson1)).thenReturn(lessonDto1);
        when(lessonMapper.toGetLessonDto(lesson2)).thenReturn(lessonDto2);

        List<GetLessonDto> result = lessonService.getAllLessons();

        // Then
        assertEquals(2, result.size());
        verify(lessonRepository, times(1)).findAll();
    }
}