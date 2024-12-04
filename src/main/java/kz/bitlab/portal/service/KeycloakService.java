package kz.bitlab.portal.service;

import jakarta.ws.rs.core.Response;
import kz.bitlab.portal.model.dto.userDto.*;
import kz.bitlab.portal.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakService {

    private final Keycloak keycloak;
    private final RestTemplate restTemplate;

    @Value("${keycloak.url}")
    private String url;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client}")
    private String client;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public UserRepresentation createUser(UserCreateDto user){
        UserRepresentation newUser = new UserRepresentation();
        newUser.setEmail(user.getEmail());
        newUser.setEmailVerified(true);
        newUser.setUsername(user.getUsername());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(user.getPassword());
        credential.setTemporary(false);

        newUser.setCredentials(List.of(credential));

        Response response = keycloak
                .realm(realm)
                .users()
                .create(newUser);

        if (response.getStatus() != HttpStatus.CREATED.value()) {
            String responseBody = response.readEntity(String.class);
            log.error("Ошибка при создании пользователя. Ответ: {}", responseBody);
            throw new RuntimeException("Ошибка создание пользователя");
        }

        List<UserRepresentation> searchUsers = keycloak.realm(realm).users().search(user.getUsername());

        return searchUsers.get(0);
    }


    public String sighIn(UserSignInDto userSignInDto){

        String tokenEndpoint = url + "/realms/" + realm + "/protocol/openid-connect/token";
        MultiValueMap<String, String> formDate = new LinkedMultiValueMap<>();

        formDate.add("grant_type", "password");
        formDate.add("client_id", client);
        formDate.add("client_secret", clientSecret);
        formDate.add("username", userSignInDto.getUsername());
        formDate.add("password", userSignInDto.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenEndpoint, new HttpEntity<>(formDate, headers), Map.class);

        Map<String, Object> responseBody = response.getBody();

        if (!response.getStatusCode().is2xxSuccessful() || responseBody == null){
            log.error("Ошибка в регистрации");
            throw new RuntimeException("Failed to authenticate");
        }

        return "\"access_token:\" " + responseBody.get("access_token") + "\n\"refresh_token: " + responseBody.get(
                "refresh_token");
    }

    public void changePassword(String username, String newPassword){
        List<UserRepresentation> users = keycloak
                .realm(realm)
                .users()
                .search(username);

        if (users.isEmpty()){
            log.error("User not found to change");
            throw new RuntimeException("User not found with username " + username);
        }

        UserRepresentation userRepresentation = users.get(0);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(newPassword);
        credentialRepresentation.setTemporary(false);

        keycloak
                .realm(realm)
                .users()
                .get(userRepresentation.getId())
                .resetPassword(credentialRepresentation);

        log.info("Password changed!");
    }

    public String refreshToken(RefreshTokenDto refreshTokenDto){
        String tokenEndpoints = url + "/realms/" + realm + "/protocol/openid-connect/token";
        MultiValueMap<String, String> formDataTwo = new LinkedMultiValueMap<>();
        formDataTwo.add("grant_type", "password");
        formDataTwo.add("client_id", client);
        formDataTwo.add("client_secret", clientSecret);
        formDataTwo.add("username", refreshTokenDto.getUsername());
        formDataTwo.add("password", refreshTokenDto.getPassword());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type","application/x-www-form-urlencoded");

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenEndpoints, new HttpEntity<>(formDataTwo, httpHeaders), Map.class);
        Map<String, Object> responseBody = response.getBody();

        if (!response.getStatusCode().is2xxSuccessful() || responseBody == null) {
            log.error("Error on refreshToken");
            throw new RuntimeException("Failed to authenticate");
        }

        return "refresh_token: " + responseBody.get("refresh_token");
    }

    public ResponseEntity<?> updateUserDto(UserUpdateDto userUpdateDto){
        String username = UserUtils.getCurrentUserName();

        List<UserRepresentation> foundUsers = keycloak.realm(realm)
                .users()
                .search(username);

        Optional<UserRepresentation> user = foundUsers.stream()
                .filter(u -> username.equalsIgnoreCase(u.getUsername()))
                .findFirst();

        UserRepresentation user1 = user.get();
        user1.setEmail(userUpdateDto.getEmail());
        user1.setFirstName(userUpdateDto.getFirstName());
        user1.setLastName(userUpdateDto.getLastName());

        keycloak.realm(realm)
                .users()
                .get(user1.getId())
                .update(user1);

        return ResponseEntity.ok("Updated user!");
    }


    public ResponseEntity<?> setRoles(UserSetRoleDto userSetRoleDto){

        List<UserRepresentation> usernames = keycloak
                .realm(realm)
                .users()
                .search(userSetRoleDto.getUsername());

        Optional<UserRepresentation> user = usernames.stream()
                .filter(username -> userSetRoleDto.getUsername().equalsIgnoreCase(username.getUsername()))
                .findFirst();

        UserRepresentation user1 = user.get();

        if (user1 == null) {
            log.error("User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        for (String role : userSetRoleDto.getRoles()) {
            if (findRoleByName(role) == null) {
                RoleRepresentation roleRepresentation = new RoleRepresentation();
                roleRepresentation.setName(role);
                keycloak.realm(realm)
                        .roles()
                        .create(roleRepresentation);

                keycloak.realm(realm)
                        .users()
                        .get(user1.getId())
                        .roles()
                        .realmLevel()
                        .add(List.of(keycloak.realm(realm).roles().get(role).toRepresentation()));
            } else {
                keycloak.realm(realm)
                        .users()
                        .get(user1.getId())
                        .roles()
                        .realmLevel()
                        .add(List.of(keycloak.realm(realm).roles().get(role).toRepresentation()));
            }
        }

        return ResponseEntity.ok("Roles set");
    }

    public RoleRepresentation findRoleByName(String name) {

        List<RoleRepresentation> foundRoles = keycloak.realm(realm)
                .roles()
                .list();

        Optional<RoleRepresentation> role = foundRoles.stream()
                .filter(r -> name.equals(r.getName()))
                .findFirst();

        if (role.isEmpty()) {
            log.info("Role '{}' not found", name);
            return null;
        }

        return role.get();
    }

    public UserRepresentation setupUserRepresentation(UserCreateDto createUserDto) {

        UserRepresentation user = new UserRepresentation();
        user.setEmail(createUserDto.getEmail());
        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setUsername(createUserDto.getUsername());
        user.setFirstName(createUserDto.getFirstName());
        user.setLastName(createUserDto.getLastName());

        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(createUserDto.getPassword());
        credentials.setTemporary(false);

        user.setCredentials(List.of(credentials));
        return user;
    }

    public UserRepresentation findUserByUsername(String username) {

        List<UserRepresentation> foundUsers = keycloak.realm(realm)
                .users()
                .search(username);

        Optional<UserRepresentation> user = foundUsers.stream()
                .filter(u -> username.equalsIgnoreCase(u.getUsername()))
                .findFirst();

        if (user.isEmpty()) {
            log.info("User '{}' not found", username);
            return null;
        }
        return user.get();
    }

}
