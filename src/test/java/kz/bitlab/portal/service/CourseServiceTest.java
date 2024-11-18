package kz.bitlab.portal.service;

import kz.bitlab.portal.exception.CourseNotFoundException;
import kz.bitlab.portal.model.dto.chapterDto.GetChapterDto;
import kz.bitlab.portal.model.dto.courseDto.CreateCourseDto;
import kz.bitlab.portal.model.dto.courseDto.GetCourseDto;
import kz.bitlab.portal.model.dto.courseDto.UpdatedCourseDto;
import kz.bitlab.portal.model.entities.Chapter;
import kz.bitlab.portal.model.entities.Course;
import kz.bitlab.portal.model.mapper.ChapterMapper;
import kz.bitlab.portal.model.mapper.CourseMapper;
import kz.bitlab.portal.repository.ChapterRepository;
import kz.bitlab.portal.repository.CourseRepository;
import kz.bitlab.portal.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CourseServiceTest {
    @InjectMocks
    private CourseServiceImpl courseService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private ChapterMapper chapterMapper;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCourse(){
        // Given
        CreateCourseDto createCourseDto = new CreateCourseDto();
        createCourseDto.setName("New Course");

        Course course = new Course();

        // When
        when(courseMapper.createCourseDtoToEntity(createCourseDto)).thenReturn(course);
        courseService.createCourse(createCourseDto);

        // Then
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testUpdatedCourse(){
        // Given
        Long courseId = 1L;
        UpdatedCourseDto updatedCourseDto = new UpdatedCourseDto();
        updatedCourseDto.setName("Updated Course");

        Course course = new Course();
        course.setId(courseId);
        course.setName("Old Course");
        course.setCreatedTime(LocalDateTime.now());

        // When
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseRepository.save(course)).thenReturn(course);
        courseService.updateCourse(courseId, updatedCourseDto);

        // Then
        assertEquals("Updated Course", course.getName());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testUpdateCourseNotFound(){
        // Given
        Long courseId = 1L;
        UpdatedCourseDto updatedCourseDto = new UpdatedCourseDto();

        // When
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Then
        assertThrows(CourseNotFoundException.class, ()-> courseService.updateCourse(courseId, updatedCourseDto));
        verify(courseRepository, never()).save(any());
    }

    @Test
    void testDeleteCourseNotFound(){
        // Given
        Long courseId = 1L;

        // When
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Then
        assertThrows(CourseNotFoundException.class, ()-> courseService.deleteCourse(courseId));
        verify(courseRepository, never()).delete(any());
    }

    @Test
    void testGetCourseById() {
        // Given
        Long courseId = 1L;

        Course course = new Course();
        course.setId(courseId);

        Chapter chapter1 = new Chapter();
        Chapter chapter2 = new Chapter();
        List<Chapter> chapters = Arrays.asList(chapter1, chapter2);

        GetCourseDto courseDto = new GetCourseDto();
        GetChapterDto chapterDto1 = new GetChapterDto();
        GetChapterDto chapterDto2 = new GetChapterDto();

        // When
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(chapterRepository.findAllByCourseIdOrderByOrder(courseId)).thenReturn(chapters);
        when(courseMapper.toGetCourseDto(course)).thenReturn(courseDto);
        when(chapterMapper.toDtoList(chapters)).thenReturn(Arrays.asList(chapterDto1, chapterDto2));

        GetCourseDto result = courseService.getCourseById(courseId);

        // Then
        assertNotNull(result);
        verify(courseRepository, times(1)).findById(courseId);
        verify(chapterRepository, times(1)).findAllByCourseIdOrderByOrder(courseId);
    }

    @Test
    void testGetCourseByIdNotFound() {
        // Given
        Long courseId = 1L;

        // When
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Then
        assertThrows(CourseNotFoundException.class, () -> courseService.getCourseById(courseId));
        verify(chapterRepository, never()).findAllByCourseIdOrderByOrder(any());
    }

    @Test
    void testGetAllCourse() {
        // Given
        Course course1 = new Course();
        Course course2 = new Course();
        List<Course> courses = Arrays.asList(course1, course2);

        GetCourseDto courseDto1 = new GetCourseDto();
        GetCourseDto courseDto2 = new GetCourseDto();

        // When
        when(courseRepository.findAll()).thenReturn(courses);
        when(courseMapper.toGetCourseDto(course1)).thenReturn(courseDto1);
        when(courseMapper.toGetCourseDto(course2)).thenReturn(courseDto2);

        List<GetCourseDto> result = courseService.getAllCourse();

        // Then
        assertEquals(2, result.size());
        verify(courseRepository, times(1)).findAll();
    }
}
















