package kz.bitlab.portal.dto.chapterDto;

import kz.bitlab.portal.model.Course;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UpdatedChapterDto {
    private String name;

    private String desciption;

    private Long id;
}
