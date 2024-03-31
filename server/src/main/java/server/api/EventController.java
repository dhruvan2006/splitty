package server.api;

import commons.Event;
import commons.Participant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/api/event")
public class EventController {

    private final EventRepository repo;

    EventController(EventRepository repo){
        this.repo = repo;
    }

    @GetMapping("")
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(repo.findAll());
    }
    
    @GetMapping("/id/{id}")
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


    @GetMapping("/invite/{inviteCode}")
    public ResponseEntity<List<Event>> getByInviteCode(@PathVariable("inviteCode") String inviteCode){
        if(inviteCode.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        List<Event> resp = repo.findAll().stream().filter(x -> x.getInviteCode().equals(inviteCode)).toList();
        return ResponseEntity.ok(resp);
    }

    //initially expenses are empty
    @PostMapping({"", "/"})
    public ResponseEntity<Event> createEvent(@RequestBody Event e) {
        if (e == null || !e.checkNull())
            return ResponseEntity.badRequest().build();
        Event saved = repo.save(e);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping({"", "/"})
    public ResponseEntity<Void> deleteEvent(@RequestParam("id") Long id) {
        if (id < 0 || !repo.existsById(id))
            return ResponseEntity.badRequest().build();

        repo.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/title")
    public ResponseEntity<Event> updateEventTitle(@PathVariable("id") long id, @RequestBody String newTitle) {
        if (newTitle == null || newTitle.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return repo.findById(id).map(event -> {
            event.setTitle(newTitle);
            Event updatedEvent = repo.save(event);
            return ResponseEntity.ok(updatedEvent);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{eventId}/participants")
    public ResponseEntity<Event> addParticipantToEvent(@PathVariable("eventId") Long eventId, @RequestBody Participant participant) {
        return repo.findById(eventId).map(event -> {
            event.addParticipant(participant);
            Event updateEvent = repo.save(event);
            return ResponseEntity.ok(updateEvent);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{eventId}/participants/{participantId}")
    public ResponseEntity<Event> removeParticipantFromEvent(@PathVariable("eventId") Long eventId, @PathVariable("participantId") Long participantId) {
        return repo.findById(eventId).map(event -> {
            boolean removed = event.removeParticipant(participantId);
            if (!removed) {
                return new ResponseEntity<Event>(HttpStatus.NOT_FOUND); // had problems with type casting
            }
            Event updatedEvent = repo.save(event);
            return ResponseEntity.ok(updatedEvent);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{eventId}/participants/{participantId}")
    public ResponseEntity<Event> updateParticipantInEvent(@PathVariable("eventId") Long eventId, @PathVariable("participantId") Long participantId, @RequestBody Participant updatedParticipant) {
        return repo.findById(eventId).map(event -> {
            boolean updated = event.updateParticipant(participantId, updatedParticipant);
            if (!updated) {
                return new ResponseEntity<Event>(HttpStatus.NOT_FOUND); // had problems with type casting
            }
            Event updatedEvent = repo.save(event);
            return ResponseEntity.ok(updatedEvent);
        }).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/{id}/participant")
    public ResponseEntity<List<Participant>> getParticipants(@PathVariable("id") Long id){
        var event = repo.findById(id);
        if(event.isEmpty())
            return ResponseEntity.badRequest().build();

        var participants = event.get().getParticipants();
        return ResponseEntity.ok(participants);
    }

    @PutMapping("/{eventId}/date")
    public ResponseEntity<Event> updateEventLastAccess(@PathVariable("eventId") Long id){
        return repo.findById(id).map(event -> {
            event.setLastUsed(Timestamp.valueOf(LocalDateTime.now()));
            Event updatedEvent = repo.save(event);
            return ResponseEntity.ok(updatedEvent);
        }).orElse(ResponseEntity.notFound().build());
    }
}
