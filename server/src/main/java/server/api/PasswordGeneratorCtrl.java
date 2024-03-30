package server.api;

import commons.PasswordGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import server.database.PasswordGeneratorRepository;

import java.util.List;

@Controller
@RequestMapping("api/password")
public class PasswordGeneratorCtrl {

    private final PasswordGeneratorRepository repo;

    PasswordGeneratorCtrl(PasswordGeneratorRepository repo){
        this.repo = repo;
    }

    /**
     * sorted by name, ascending
     */
    @GetMapping("")
    public ResponseEntity<List<PasswordGenerator>> getPassword() {
        return ResponseEntity.ok(repo.findAll());
    }

    @PostMapping({"", "/"})
    public ResponseEntity<PasswordGenerator> postPassword(@RequestBody PasswordGenerator pg){
        PasswordGenerator saved = repo.save(pg);
        return ResponseEntity.ok(saved);
    }
}
