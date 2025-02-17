package co.com.api.rest.controller;

import co.com.api.rest.controller.dto.UserDTO;
import co.com.api.rest.service.IKeycloakService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/keycloak/user")
@PreAuthorize("hasRole('R_ADMIN_CLIENT')")
public class KeycloakController {
    private final IKeycloakService keycloakService;

    public KeycloakController(IKeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAllUsers() {
        return ResponseEntity.ok(keycloakService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findUserById(String id) {
        return ResponseEntity.ok(keycloakService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(keycloakService.create(userDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        keycloakService.update(id, userDTO);
        return ResponseEntity.ok().build();
    }

     @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
         keycloakService.delete(id);
         return ResponseEntity.ok().build();
     }
}
