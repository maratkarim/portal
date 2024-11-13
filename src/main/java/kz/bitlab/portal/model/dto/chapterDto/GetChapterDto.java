package kz.bitlab.portal.model.dto.chapterDto;

import kz.bitlab.portal.model.dto.lessonDto.GetLessonDto;
import kz.bitlab.portal.model.entities.Course;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GetChapterDto {
    private Long id;

    private String name;

    private String description;

    private int order;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private Course course;

    private List<GetLessonDto> lessons;
}
