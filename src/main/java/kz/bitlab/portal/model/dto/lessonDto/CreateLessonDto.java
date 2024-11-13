package kz.bitlab.portal.model.dto.lessonDto;

import lombok.Data;

@Data
public class CreateLessonDto {
    private String name;

    private String description;

    private String context;

    private Long chapterId;
}
