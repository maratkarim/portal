package kz.bitlab.portal.model.dto.lessonDto;

import kz.bitlab.portal.model.entities.Chapter;
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
