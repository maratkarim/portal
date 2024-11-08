package kz.bitlab.portal.dto.chapterDto;

import kz.bitlab.portal.model.Course;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetChapterDto {
    private Long id;

    private String name;

    private String desciption;

    private int order;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private Course course;
}
