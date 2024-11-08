package kz.bitlab.portal.dto.lessonDto;

import kz.bitlab.portal.model.Chapter;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetLessonDto {
    private Long id;

    private String name;

    private String description;

    private String context;

    private int order;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private Chapter chapter;
}
