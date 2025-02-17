package co.com.api.rest.util;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeycloakProvider {
    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.realm-master}")
    private String realmMaster;
    @Value("${keycloak.cli}")
    private String adminCli;
    @Value("${keycloak.user-console}")
    private String userConsole;
    @Value("${keycloak.user-password}")
    private String userPassword;
    @Value("${keycloak.client-secret}")
    private String clientSecret;


    public RealmResource getRealmResource() {
        Keycloak keycloak= KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm(realmMaster)
                .clientId(adminCli)
                .username(userConsole)
                .password(userPassword)
                .clientSecret(clientSecret)
                .resteasyClient(
                        new ResteasyClientBuilderImpl()
                                .connectionPoolSize(10)
                                .build()
                )
                .build();
        return keycloak.realm(realm);
    }

    public UsersResource getUserResource() {
        return getRealmResource().users();
    }
}
