package kz.bitlab.portal.model.dto.courseDto;

import kz.bitlab.portal.model.dto.chapterDto.GetChapterDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GetCourseDto {
    private Long id;

    private String name;

    private String description;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    // temporal
    private List<GetChapterDto> chapters;
}
