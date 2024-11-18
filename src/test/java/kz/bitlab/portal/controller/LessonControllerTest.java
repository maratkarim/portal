package kz.bitlab.portal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.bitlab.portal.model.dto.lessonDto.CreateLessonDto;
import kz.bitlab.portal.model.dto.lessonDto.GetLessonDto;
import kz.bitlab.portal.model.dto.lessonDto.UpdatedLessonDto;
import kz.bitlab.portal.service.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(LessonController.class)
public class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService lessonService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGellAllLesson() throws Exception{
        // Given
        GetLessonDto lesson1 = new GetLessonDto();
        lesson1.setId(1L);
        GetLessonDto lesson2 = new GetLessonDto();
        lesson2.setId(2L);
        List<GetLessonDto> lessons = Arrays.asList(lesson1, lesson2);

        // When
        when(lessonService.getAllLessons()).thenReturn(lessons);

        // Then
        mockMvc.perform(get("/lesson/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(lessons.size()))
                .andExpect(jsonPath("$[0].id").value(lesson1.getId()))
                .andExpect(jsonPath("$[1].id").value(lesson2.getId()));

        verify(lessonService, times(1)).getAllLessons();
    }

    @Test
    void testGetLessonById() throws Exception{
        // Given
        Long lessonId = 1L;
        GetLessonDto lesson = new GetLessonDto();
        lesson.setId(lessonId);

        // When
        when(lessonService.getLessonById(lessonId)).thenReturn(lesson);

        // Then
        mockMvc.perform(get("/lesson/{id}", lessonId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(lessonId));

        verify(lessonService, times(1)).getLessonById(lessonId);
    }

    @Test
    void testCreateLesson() throws Exception {
        // Given
        CreateLessonDto createLessonDto = new CreateLessonDto();
        createLessonDto.setName("New Lesson");

        // When
        doNothing().when(lessonService).createLesson(createLessonDto);

        // Then
        mockMvc.perform(post("/lesson/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createLessonDto)))
                .andExpect(status().isOk());

        verify(lessonService, times(1)).createLesson(createLessonDto);
    }

    @Test
    void testUpdateLesson() throws Exception {
        // Given
        Long lessonId = 1L;
        UpdatedLessonDto updatedLessonDto = new UpdatedLessonDto();
        updatedLessonDto.setName("Updated Lesson");

        // When
        doNothing().when(lessonService).updateLesson(lessonId, updatedLessonDto);

        // Then
        mockMvc.perform(put("/lesson/update/{id}", lessonId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedLessonDto)))
                .andExpect(status().isOk());

        verify(lessonService, times(1)).updateLesson(lessonId, updatedLessonDto);
    }

    @Test
    void testDeleteLesson() throws Exception {
        // Given
        Long lessonId = 1L;

        // When
        doNothing().when(lessonService).deleteLesson(lessonId);

        // Then
        mockMvc.perform(delete("/lesson/delete/{id}", lessonId))
                .andExpect(status().isOk());

        verify(lessonService, times(1)).deleteLesson(lessonId);
    }

}
