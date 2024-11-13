package kz.bitlab.portal.model.dto.chapterDto;

import lombok.Data;

@Data
public class UpdatedChapterDto {
    private String name;

    private String description;

    private Long courseId;
}
