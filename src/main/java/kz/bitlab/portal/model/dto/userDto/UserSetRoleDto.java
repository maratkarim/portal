package kz.bitlab.portal.model.dto.userDto;

import lombok.Data;

import java.util.List;

@Data
public class UserSetRoleDto {

    private String username;
    private List<String> roles;

}
