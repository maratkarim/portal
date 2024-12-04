package kz.bitlab.portal.model.dto.userDto;

import lombok.Data;

@Data
public class UserUpdateDto {
    private String email;
    private String firstName;
    private String lastName;
}
