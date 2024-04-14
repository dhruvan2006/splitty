package server.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.Main;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final String adminPassword = Main.getPassword();

    @PostMapping("/authenticate")
    public boolean authenticate(@RequestParam String password) {
        return password.equals(adminPassword);
    }

    @PostMapping("/changePassword")
    public boolean changePassword(@RequestBody String newPassword) {
        if (newPassword == null || newPassword.isEmpty()) {
            return false;
        }
        Main.setPassword(newPassword);
        adminPassword = newPassword;
        
        return true;
    }
    
}
