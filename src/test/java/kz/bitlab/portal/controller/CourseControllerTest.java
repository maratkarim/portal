package kz.bitlab.portal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.bitlab.portal.model.dto.courseDto.CreateCourseDto;
import kz.bitlab.portal.model.dto.courseDto.GetCourseDto;
import kz.bitlab.portal.model.dto.courseDto.UpdatedCourseDto;
import kz.bitlab.portal.service.CourseService;
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


@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Autowired
    private  ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCourse() throws Exception {
        // Given
        GetCourseDto courseDto1 = new GetCourseDto();
        courseDto1.setId(1L);

        GetCourseDto courseDto2 = new GetCourseDto();
        courseDto2.setId(2L);

        List<GetCourseDto> courseDtoList = Arrays.asList(courseDto1, courseDto2);

        // When
        when(courseService.getAllCourse()).thenReturn(courseDtoList);

        // Then
        mockMvc.perform(get("/course/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(courseDtoList.size()))
                .andExpect(jsonPath("$[0].id").value(courseDto1.getId()))
                .andExpect(jsonPath("$[1].id").value(courseDto2.getId()));

        verify(courseService, times(1)).getAllCourse();
    }

    @Test
    void testGetCouseDto() throws Exception{
        // Given
        Long courseId = 1L;
        GetCourseDto courseDto = new GetCourseDto();
        courseDto.setId(courseId);

        // When
        when(courseService.getCourseById(courseId)).thenReturn(courseDto);

        // Then
        mockMvc.perform(get("/course/{id}", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(courseId));

        verify(courseService, times(1)).getCourseById(courseId);
    }

    @Test
    void testCreateCourse() throws Exception {
        // Given
        CreateCourseDto createCourseDto = new CreateCourseDto();
        createCourseDto.setName("New Course");

        // When
        doNothing().when(courseService).createCourse(createCourseDto);

        // Then
        mockMvc.perform(post("/course/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCourseDto)))
                .andExpect(status().isCreated());

        verify(courseService, times(1)).createCourse(createCourseDto);
    }

    @Test
    void testUpdateCourse() throws Exception {
        // Given
        Long courseId = 1L;
        UpdatedCourseDto updatedCourseDto = new UpdatedCourseDto();
        updatedCourseDto.setName("Updated Course");

        // When
        doNothing().when(courseService).updateCourse(courseId, updatedCourseDto);

        // Then
        mockMvc.perform(put("/course/update/{id}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCourseDto)))
                .andExpect(status().isOk());

        verify(courseService, times(1)).updateCourse(courseId, updatedCourseDto);
    }

    @Test
    void testDeleteCourse() throws Exception {
        // Given
        Long courseId = 1L;

        // When
        doNothing().when(courseService).deleteCourse(courseId);

        // Then
        mockMvc.perform(delete("/course/delete/{id}", courseId))
                .andExpect(status().isOk());

        verify(courseService, times(1)).deleteCourse(courseId);
    }

}
