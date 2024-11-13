package kz.bitlab.portal.model.dto.chapterDto;

import lombok.Data;

@Data
public class CreateChapterDto {
    private String name;

    private String description;

    private Long courseId;
}
