package kz.bitlab.portal.dto.lessonDto;

import lombok.Data;

@Data
public class CreateLessonDto {
    private String name;

    private String description;

    private String context;
}
