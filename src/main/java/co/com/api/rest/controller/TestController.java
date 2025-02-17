package co.com.api.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
public class TestController {


    @GetMapping("/admin")
    @PreAuthorize("hasRole('R_ADMIN_CLIENT')")
    public ResponseEntity<String> admin() {
        return ResponseEntity.ok("Hello Admin");
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('R_USER_CLIENT','R_ADMIN_CLIENT')")
    public ResponseEntity<String> user() {
        return ResponseEntity.ok("Hello User");
    }
}
