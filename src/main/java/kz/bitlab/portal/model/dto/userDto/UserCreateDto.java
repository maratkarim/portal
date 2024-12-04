package kz.bitlab.portal.model.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserCreateDto {

    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String password;

}
