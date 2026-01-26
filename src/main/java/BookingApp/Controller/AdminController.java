package BookingApp.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public ResponseEntity<String> adminDashboard(Authentication authentication) {

        String adminEmail = authentication.getName();

        return ResponseEntity.ok(
                "Welcome ADMIN: " + adminEmail
        );
    }

    @GetMapping("/health")
    public ResponseEntity<String> adminHealth() {
        return ResponseEntity.ok("Admin service is running");
    }
}