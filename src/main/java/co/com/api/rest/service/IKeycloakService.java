package co.com.api.rest.service;

import co.com.api.rest.controller.dto.UserDTO;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface IKeycloakService {
    List<UserRepresentation> findAll();
    List<UserRepresentation> findById(String id);
    String create(UserDTO userDTO);
    void update(String id, UserDTO userDTO);
    void delete(String id);
}
