package kz.bitlab.portal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.bitlab.portal.model.dto.chapterDto.CreateChapterDto;
import kz.bitlab.portal.model.dto.chapterDto.GetChapterDto;
import kz.bitlab.portal.model.dto.chapterDto.UpdatedChapterDto;
import kz.bitlab.portal.service.ChapterService;
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

@WebMvcTest(ChapterController.class)
class ChapterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChapterService chapterService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllChapters() throws Exception {
        // Given
        GetChapterDto chapter1 = new GetChapterDto();
        chapter1.setId(1L);
        GetChapterDto chapter2 = new GetChapterDto();
        chapter2.setId(2L);
        List<GetChapterDto> chapters = Arrays.asList(chapter1, chapter2);

        // When
        when(chapterService.getAllChapter()).thenReturn(chapters);

        // Then
        mockMvc.perform(get("/chapter/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(chapters.size()))
                .andExpect(jsonPath("$[0].id").value(chapter1.getId()))
                .andExpect(jsonPath("$[1].id").value(chapter2.getId()));

        verify(chapterService, times(1)).getAllChapter();
    }

    @Test
    void testGetChapterById() throws Exception {
        // Given
        Long chapterId = 1L;
        GetChapterDto chapter = new GetChapterDto();
        chapter.setId(chapterId);

        // When
        when(chapterService.getChapterById(chapterId)).thenReturn(chapter);

        // Then
        mockMvc.perform(get("/chapter/{id}", chapterId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(chapterId));

        verify(chapterService, times(1)).getChapterById(chapterId);
    }

    @Test
    void testCreateChapter() throws Exception {
        // Given
        CreateChapterDto createChapterDto = new CreateChapterDto();
        createChapterDto.setName("New Chapter");

        // When
        doNothing().when(chapterService).createChapter(createChapterDto);

        // Then
        mockMvc.perform(post("/chapter/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createChapterDto)))
                .andExpect(status().isOk());

        verify(chapterService, times(1)).createChapter(createChapterDto);
    }

    @Test
    void testUpdateChapter() throws Exception {
        // Given
        Long chapterId = 1L;
        UpdatedChapterDto updatedChapterDto = new UpdatedChapterDto();
        updatedChapterDto.setName("Updated Chapter");

        // When
        doNothing().when(chapterService).updateChapter(chapterId, updatedChapterDto);

        // Then
        mockMvc.perform(put("/chapter/update/{id}", chapterId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedChapterDto)))
                .andExpect(status().isOk());

        verify(chapterService, times(1)).updateChapter(chapterId, updatedChapterDto);
    }

    @Test
    void testDeleteChapter() throws Exception {
        // Given
        Long chapterId = 1L;

        // When
        doNothing().when(chapterService).deleteChapter(chapterId);

        // Then
        mockMvc.perform(delete("/chapter/delete/{id}", chapterId))
                .andExpect(status().isOk());

        verify(chapterService, times(1)).deleteChapter(chapterId);
    }
}
