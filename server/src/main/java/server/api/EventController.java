package server.api;

import commons.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Controller
@RequestMapping("/api/event")
public class EventController {
    private final EventRepository repo;
    EventController(EventRepository repo){
        this.repo = repo;
    }

    @GetMapping("")
    public ResponseEntity<List<Event>> getAllEvents(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "id") String sortBy,
                                                    @RequestParam(defaultValue = "asc") String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Event> eventPage = repo.findAll(pageable);
        return ResponseEntity.ok(eventPage.getContent());
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
