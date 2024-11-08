package kz.bitlab.portal.dto.courseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCourseDto {
    private String name;

    private String description;
}
