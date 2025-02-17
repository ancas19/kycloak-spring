package co.com.api.rest.service.impl;

import co.com.api.rest.controller.dto.UserDTO;
import co.com.api.rest.service.IKeycloakService;
import co.com.api.rest.util.KeycloakProvider;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class KeycloakImpl  implements IKeycloakService {
    private final KeycloakProvider keycloakProvider;

    public KeycloakImpl(KeycloakProvider keycloakProvider) {
        this.keycloakProvider = keycloakProvider;
    }

    /*
    * This method is used to get all users from keycloak
    * @return List<UserRepresentation>
     */
    @Override
    public List<UserRepresentation> findAll() {
        return keycloakProvider.getRealmResource()
                .users()
                .list();
    }


    /*
    * This method is used to get a user by id from keycloak
    * @param id
    * @return UserRepresentation
     */
    @Override
    public List<UserRepresentation> findById(String id) {
        return keycloakProvider.getRealmResource()
                .users()
                .searchByUsername(id,true);
    }


    /*
    * This method is used to create a user in keycloak
    * @param userDTO
    * @return String
     */
    @Override
    public String create(UserDTO userDTO) {
        UsersResource userResource = keycloakProvider.getUserResource();
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userDTO.username());
        userRepresentation.setEmail(userDTO.email());
        userRepresentation.setFirstName(userDTO.firstName());
        userRepresentation.setLastName(userDTO.lastName());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);

        Response response=userResource.create(userRepresentation);
        switch (response.getStatus()) {
            case 201:
                 return setPassword(response, userResource, userDTO);
            case 409:
                log.error("User already exists");
                return "User already exists";
            default:
                log.error("Error creating user");
                return "Error creating user, plese check the logs";
        }
    }


    @Override
    public void update(String id, UserDTO userDTO) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        credentialRepresentation.setValue(userDTO.password());
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userDTO.username());
        userRepresentation.setEmail(userDTO.email());
        userRepresentation.setFirstName(userDTO.firstName());
        userRepresentation.setLastName(userDTO.lastName());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
        UserResource userResource = keycloakProvider.getUserResource().get(id);
        userResource.update(userRepresentation);
    }

    /**
     * This method is used to delete a user in keycloak
     * @param id
     */
    @Override
    public void delete(String id) {
        UserResource userResource = keycloakProvider.getUserResource().get(id);
        userResource.remove();
    }

    private String setPassword(Response response, UsersResource userResource,UserDTO userDTO) {
        String path = response.getLocation().getPath();
        String userId = path.substring(path.lastIndexOf('/') + 1);
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        credentialRepresentation.setValue(userDTO.password());
        userResource.get(userId).resetPassword(credentialRepresentation);
        RealmResource realmResource = keycloakProvider.getRealmResource();
        List<RoleRepresentation> roles = null;
        if (Objects.isNull(userDTO.roles()) || userDTO.roles().isEmpty()) {
            roles = List.of(realmResource.roles().get("user").toRepresentation());
        } else
            roles = realmResource.roles().list().stream()
                    .filter(role -> userDTO.roles().contains(role.getName()))
                    .toList();

        realmResource.users().get(userId)
                .roles()
                .realmLevel()
                .add(roles);
        return "User created successfully";
    }
}
