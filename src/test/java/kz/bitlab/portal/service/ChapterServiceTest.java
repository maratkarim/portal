package kz.bitlab.portal.service;

import kz.bitlab.portal.exception.ChapterNotFoundException;
import kz.bitlab.portal.model.dto.chapterDto.CreateChapterDto;
import kz.bitlab.portal.model.dto.chapterDto.GetChapterDto;
import kz.bitlab.portal.model.dto.chapterDto.UpdatedChapterDto;
import kz.bitlab.portal.model.dto.lessonDto.GetLessonDto;
import kz.bitlab.portal.model.entities.Chapter;
import kz.bitlab.portal.model.entities.Course;
import kz.bitlab.portal.model.entities.Lesson;
import kz.bitlab.portal.model.mapper.ChapterMapper;
import kz.bitlab.portal.model.mapper.LessonMapper;
import kz.bitlab.portal.repository.ChapterRepository;
import kz.bitlab.portal.repository.CourseRepository;
import kz.bitlab.portal.repository.LessonRepository;
import kz.bitlab.portal.service.impl.ChapterServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChapterServiceTest {

    @InjectMocks
    private ChapterServiceImpl chapterService;

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private ChapterMapper chapterMapper;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private LessonMapper lessonMapper;

    public ChapterServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateChapter() {
        // Given
        CreateChapterDto createChapterDto = new CreateChapterDto();
        createChapterDto.setCourseId(1L);

        Course course = new Course();
        Chapter chapter = new Chapter();

        when(courseRepository.findById(createChapterDto.getCourseId())).thenReturn(Optional.of(course));
        when(chapterMapper.createChapterDtoToEntity(createChapterDto)).thenReturn(chapter);
        when(chapterRepository.countByCourseId(createChapterDto.getCourseId())).thenReturn(2);

        // When
        chapterService.createChapter(createChapterDto);

        // Then
        verify(chapterRepository, times(1)).save(chapter);
        assertEquals(course, chapter.getCourse());
        assertEquals(3, chapter.getOrder()); // Порядок новой главы
        assertNotNull(chapter.getCreatedTime());
        assertNotNull(chapter.getUpdatedTime());
    }

    @Test
    void testUpdateChapter() {
        // Given
        Long chapterId = 1L;
        UpdatedChapterDto updatedChapterDto = new UpdatedChapterDto();
        updatedChapterDto.setName("Updated Chapter");
        updatedChapterDto.setCourseId(2L);

        Chapter chapter = new Chapter();
        Course course = new Course();

        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(courseRepository.findById(updatedChapterDto.getCourseId())).thenReturn(Optional.of(course));

        // When
        chapterService.updateChapter(chapterId, updatedChapterDto);

        // Then
        verify(chapterRepository, times(1)).save(chapter);
        assertEquals("Updated Chapter", chapter.getName());
        assertEquals(course, chapter.getCourse());
        assertNotNull(chapter.getUpdatedTime());
    }

    @Test
    void testUpdateChapter_ChapterNotFound() {
        // Given
        Long chapterId = 1L;
        UpdatedChapterDto updatedChapterDto = new UpdatedChapterDto();

        when(chapterRepository.findById(chapterId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ChapterNotFoundException.class, () -> chapterService.updateChapter(chapterId, updatedChapterDto));
    }

    @Test
    void testDeleteChapter() {
        // Given
        Long chapterId = 1L;
        Chapter chapter = new Chapter();

        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));

        // When
        chapterService.deleteChapter(chapterId);

        // Then
        verify(chapterRepository, times(1)).delete(chapter);
    }

    @Test
    void testGetChapterById() {
        // Given
        Long chapterId = 1L;

        Chapter chapter = new Chapter();
        Lesson lesson1 = new Lesson();
        Lesson lesson2 = new Lesson();

        List<Lesson> lessons = Arrays.asList(lesson1, lesson2);

        GetChapterDto chapterDto = new GetChapterDto();
        GetLessonDto lessonDto1 = new GetLessonDto();
        GetLessonDto lessonDto2 = new GetLessonDto();


        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(lessonRepository.findAllByChapterIdOrderById(chapterId)).thenReturn(lessons);
        when(chapterMapper.toGetChapterDto(chapter)).thenReturn(chapterDto);
        when(lessonMapper.toDtoList(lessons)).thenReturn(Arrays.asList(lessonDto1, lessonDto2));

        // When
        GetChapterDto result = chapterService.getChapterById(chapterId);

        // Then
        assertNotNull(result);
        assertEquals(chapterDto, result);
        verify(chapterRepository, times(1)).findById(chapterId);
        verify(lessonRepository, times(1)).findAllByChapterIdOrderById(chapterId);
    }

    @Test
    void testGetAllChapter() {
        // Given
        Chapter chapter1 = new Chapter();
        Chapter chapter2 = new Chapter();

        GetChapterDto chapterDto1 = new GetChapterDto();
        GetChapterDto chapterDto2 = new GetChapterDto();

        List<Chapter> chapters = Arrays.asList(chapter1, chapter2);
        when(chapterRepository.findAll()).thenReturn(chapters);
        when(chapterMapper.toGetChapterDto(chapter1)).thenReturn(chapterDto1);
        when(chapterMapper.toGetChapterDto(chapter2)).thenReturn(chapterDto2);

        // When
        List<GetChapterDto> result = chapterService.getAllChapter();

        // Then
        assertEquals(2, result.size());
        assertTrue(result.contains(chapterDto1));
        assertTrue(result.contains(chapterDto2));
    }
}
