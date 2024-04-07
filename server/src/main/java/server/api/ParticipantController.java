package server.api;

import commons.Participant;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.database.ParticipantRepository;
import java.util.List;

@Controller
@RequestMapping("/api/participant")
public class ParticipantController {
    private final ParticipantRepository repo;

    ParticipantController(ParticipantRepository repo){
        this.repo = repo;
    }

    /**
    * sorted by name, ascending
    */
    @GetMapping("")
    public ResponseEntity<List<Participant>> findAllParticipants() {
//        @RequestParam(defaultValue = "0") int page,
//        @RequestParam(defaultValue = "10") int size,
//        @RequestParam(defaultValue = "name") String sortBy,
//        @RequestParam(defaultValue = "asc") String sortOrde
//        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
//        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
//        Page<Participant> participantPage = repo.findAll(pageable);
        return ResponseEntity.ok(repo.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<List<Participant>> getById(@PathVariable("id") String id) {
        long lid;
        try {
            lid = Long.parseLong(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        if (lid < 0 || !repo.existsById(lid))
            return ResponseEntity.badRequest().build();

        List<Participant> resp = repo.findAll().stream().filter(x -> x.getId() == lid).toList();
        return ResponseEntity.ok(resp);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<Participant> createParticipant(@RequestBody Participant p) {
        if (p == null || !p.notNull())
            return ResponseEntity.badRequest().build();
        Participant saved = repo.save(p);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping({"", "/"})
    public ResponseEntity<Participant> deleteIt(@RequestParam("id") Long id) {
        if (id < 0 || !repo.existsById(id))
            return ResponseEntity.badRequest().build();

        repo.deleteById(id);
        List<Participant> resp = repo.findAll().stream().filter(x -> x.getId() == id).toList();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/searchName/{name}")
    public ResponseEntity<List<Participant>> findByName(@PathVariable String name) {
        List<Participant> participants = repo.findByUserName(name);
        if (participants.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(participants);
    }

    @GetMapping("/searchPartial/{substring}")
    public ResponseEntity<List<Participant>> findByPartialName(@PathVariable String substring) {
        List<Participant> participants = repo.findByPartialName(substring);
        if (participants.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(participants);
    }
}
