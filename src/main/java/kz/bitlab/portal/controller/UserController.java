package kz.bitlab.portal.controller;

import kz.bitlab.portal.model.dto.userDto.*;
import kz.bitlab.portal.service.KeycloakService;
import kz.bitlab.portal.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {

    private final KeycloakService keycloakService;

    @PostMapping(value = "/create")
    public UserRepresentation createUser(@RequestBody UserCreateDto userCreateDto){
        return keycloakService.createUser(userCreateDto);
    }

    @PostMapping(value = "/sign-in")
    public String signIn(@RequestBody UserSignInDto userSignInDto){
        return keycloakService.sighIn(userSignInDto);
    }

    @PostMapping(value = "/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> changePassword(@RequestBody UserChangePasswordDto userChangePasswordDto){
        String currentUserName = UserUtils.getCurrentUserName();
        if (currentUserName == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Couldn't Identify User");
        }

        try {
            keycloakService.changePassword(currentUserName, userChangePasswordDto.getNewPassword());
            return ResponseEntity.ok("Password Changed");
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error on changing password");
        }
    }

    @PostMapping(value = "/refresh-token")
    public String refreshToken(@RequestBody RefreshTokenDto refreshTokenDto){
        return keycloakService.refreshToken(refreshTokenDto);
    }

    @PutMapping(value = "/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDto userUpdateDto){
        keycloakService.updateUserDto(userUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @PutMapping(value = "/set-roles")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> setRoleToUser(@RequestBody UserSetRoleDto userSetRoleDto){
        return keycloakService.setRoles(userSetRoleDto);
    }

}
