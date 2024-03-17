package server.api;

import commons.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;

import java.util.List;

@Controller
@RequestMapping("/api/event")
public class EventController {
    private final EventRepository repo;
    EventController(EventRepository repo){
        this.repo = repo;
    }
    @GetMapping({"", "/"})
    public ResponseEntity<List<Event>> getAll() {
        return ResponseEntity.ok(repo.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<List<Event>> getById(@PathVariable("id") String id) {
        long lid;
        try {
            lid = Long.parseLong(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        if (lid < 0 || !repo.existsById(lid))
            return ResponseEntity.badRequest().build();

        List<Event> resp = repo.findAll().stream().filter(x -> x.getId() == lid).toList();
        return ResponseEntity.ok(resp);
    }
    //initially expenses are empty
    @PostMapping({"", "/"})
    public ResponseEntity<Event> postParticipant(@RequestBody Event e) {
        if (e == null || !e.isInit())
            return ResponseEntity.badRequest().build();
        Event saved = repo.save(e);
        return ResponseEntity.ok(saved);
    }
    @DeleteMapping({"", "/"})
    public ResponseEntity<Event> deleteIt(@RequestParam("id") Long id) {
        if (id < 0 || !repo.existsById(id))
            return ResponseEntity.badRequest().build();

        repo.deleteById(id);
        List<Event> resp = repo.findAll().stream().filter(x -> x.getId() == id).toList();
        return ResponseEntity.ok().build();
    }
}
