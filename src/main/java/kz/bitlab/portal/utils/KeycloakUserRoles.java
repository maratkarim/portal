package kz.bitlab.portal.utils;//package kz.bitlab.portal.utils;
//
//import jakarta.annotation.PostConstruct;
//import jakarta.ws.rs.core.Response;
//import kz.bitlab.portal.model.dto.userDto.UserCreateDto;
//import kz.bitlab.portal.model.dto.userDto.UserSetRoleDto;
//import kz.bitlab.portal.service.KeycloakService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.keycloak.admin.client.Keycloak;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.keycloak.representations.idm.UserRepresentation;
//
//import java.util.List;
//import java.util.Optional;
//
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class KeycloakUserRoles {
//
//    private final Keycloak keycloak;
//    private final KeycloakService keycloakService;
//
//    @Value("${keycloak.realm}")
//    private String realm;
//
//    @PostConstruct
//    public void addInitialUser(){
//        UserCreateDto user = new UserCreateDto();
//        user.setUsername("user");
//        user.setEmail("user1@gmail.com");
//        user.setFirstName("FirstNameUser");
//        user.setLastName("LastNameUser");
//        user.setPassword("User1234");
//
//        UserCreateDto teacher = new UserCreateDto();
//        teacher.setUsername("teacher");
//        teacher.setEmail("teacher1@gmail.com");
//        teacher.setFirstName("FirstNameTeacher");
//        teacher.setLastName("LastNameTeacher");
//        teacher.setPassword("Teacher1234");
//
//        UserCreateDto admin = new UserCreateDto();
//        admin.setUsername("admin");
//        admin.setEmail("admin1@gmail.com");
//        admin.setFirstName("FirstNameAdmin");
//        admin.setLastName("LastNameAdmin");
//        admin.setPassword("Admin1234");
//
//        createInitialUser(teacher);
//        createInitialUser(user);
//        createInitialUser(admin);
//
//        UserSetRoleDto userSetRoleDto1 = new UserSetRoleDto();
//        userSetRoleDto1.setUsername(teacher.getUsername());
//        userSetRoleDto1.setRoles(List.of("TEACHER"));
//
//        UserSetRoleDto userSetRoleDto2 = new UserSetRoleDto();
//        userSetRoleDto2.setUsername(user.getUsername());
//        userSetRoleDto2.setRoles(List.of("USER"));
//
//        UserSetRoleDto userSetRoleDto3 = new UserSetRoleDto();
//        userSetRoleDto3.setUsername(admin.getUsername());
//        userSetRoleDto3.setRoles(List.of("ADMIN"));
//
//        keycloakService.setRoles(userSetRoleDto1);
//        keycloakService.setRoles(userSetRoleDto2);
//        keycloakService.setRoles(userSetRoleDto3);
//    }
//
//    private void createInitialUser(UserCreateDto user) {
//
//        UserRepresentation foundUser = keycloakService.findUserByUsername(user.getUsername());
//
//        if (foundUser != null) {
//            log.info("User with username {} already exists", user.getUsername());
//            return;
//        }
//
//        UserRepresentation userRepresentation = keycloakService.setupUserRepresentation(user);
//
//        Response response = keycloak
//                .realm(realm)
//                .users()
//                .create(userRepresentation);
//
//        if (response.getStatus() != HttpStatus.CREATED.value()) {
//            log.error("Error on creating initial user");
//        }
//
//    }
//
//}

import jakarta.annotation.PostConstruct;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakUserRoles {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @PostConstruct
    public void addInitialUsersAndRoles() {
        try {
            // Создаем роли в Keycloak
            createRoleIfNotExists("ADMIN");
            createRoleIfNotExists("TEACHER");
            createRoleIfNotExists("USER");

            // Создаем пользователей
            createUserIfNotExists("marat_admin", "mar_karim@kbtu.kz", "1234", "ADMIN");
            createUserIfNotExists("teacher", "teacher@gmail.com", "teacherPassword", "TEACHER");
            createUserIfNotExists("user", "user@gmail.com", "userPassword", "USER");

        } catch (Exception e) {
            log.error("Error during initialization: {}", e.getMessage(), e);
        }
    }

    private void createRoleIfNotExists(String roleName) {
        if (keycloak.realm(realm).roles().get(roleName).toRepresentation() == null) {
            RoleRepresentation role = new RoleRepresentation();
            role.setName(roleName);
            keycloak.realm(realm).roles().create(role);
            log.info("Created role: {}", roleName);
        }
    }

    private void createUserIfNotExists(String username, String email, String password, String roleName) {
        UserRepresentation user = keycloak.realm(realm).users().search(username).stream().findFirst().orElse(null);

        if (user == null) {
            user = new UserRepresentation();
            user.setUsername(username);
            user.setEmail(email);
            user.setEnabled(true);

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            credential.setTemporary(false);
            user.setCredentials(Arrays.asList(credential));

            keycloak.realm(realm).users().create(user);
            log.info("Created user: {}", username);
        }

        // Назначаем роль пользователю
        keycloak.realm(realm)
                .users()
                .get(user.getId())
                .roles()
                .realmLevel()
                .add(Arrays.asList(keycloak.realm(realm).roles().get(roleName).toRepresentation()));
        log.info("Assigned role '{}' to user '{}'", roleName, username);
    }
}
