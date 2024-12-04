package kz.bitlab.portal.configuration;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class KeycloakConfig {

    @Value("${keycloak.url}")
    private String url;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client}")
    private String client;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.username}")
    private String username;

    @Value("${keycloak.password}")
    private String password;

    @Value("${keycloak.grant-type}")
    private String grantType;

    @Bean
    public Keycloak keycloak(){
        log.info("Создание Keycloak Bean");
        return KeycloakBuilder.builder()
                .serverUrl(url)
                .realm(realm)
                .clientId(client)
                .clientSecret(clientSecret)
                .username(username)
                .password(password)
                .grantType(grantType)
                .build();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
