package kz.bitlab.portal.dto.lessonDto;

import lombok.Data;

@Data
public class UpdatedLessonDto {
    private String name;

    private String description;

    private String context;

    private Long id;
}
