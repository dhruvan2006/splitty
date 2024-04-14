package server.api;

import commons.Event;
import commons.Participant;
import commons.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.database.EventRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Controller
@RequestMapping("/api/event")
public class EventController {

    private final EventRepository repo;

    @Autowired
    EventController(EventRepository repo){
        this.repo = repo;
    }

    @GetMapping("")
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(repo.findAll());
    }


    private Map<Object, Consumer<String>> listeners = new HashMap<>();
    @GetMapping("/{id}/title")
    public DeferredResult<ResponseEntity<String>> getEventTitle(@PathVariable("id") long id) {

        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<String>>(10000L, noContent);

        var key = new Object();
        listeners.put(key, t -> {
            res.setResult(ResponseEntity.ok(t));
        });

        res.onCompletion(() -> {
            listeners.remove(key);
        });

        return res;
    }

    @PutMapping("/{id}/title")
    public ResponseEntity<Event> updateEventTitle(@PathVariable("id") long id, @RequestBody String newTitle) {
        if (newTitle == null || newTitle.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        listeners.forEach((k,l) -> l.accept(newTitle));

        return repo.findById(id).map(event -> {
            event.setTitle(newTitle);
            Event updatedEvent = repo.save(event);
            return ResponseEntity.ok(updatedEvent);
        }).orElse(ResponseEntity.notFound().build());
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

    @PutMapping("/{eventId}/date")
    public ResponseEntity<Event> updateEventLastAccess(@PathVariable("eventId") Long id){
        return repo.findById(id).map(event -> {
            event.setLastUsed(Timestamp.valueOf(LocalDateTime.now()));
            Event updatedEvent = repo.save(event);
            return ResponseEntity.ok(updatedEvent);
        }).orElse(ResponseEntity.notFound().build());
    }
    @MessageMapping("/websocket/notify/event")
    @SendTo("/topic/event")
    public Event addParticipantToEventWS(@Payload Event event) {
        return event;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable("id") Long id) {
        Optional<Event> event = repo.findById(id);
        return event.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<Participant>> getParticipantsByEventId(@PathVariable("id") Long id) {
        return repo.findById(id)
                .map(event -> ResponseEntity.ok(event.getParticipants()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/expenses")
    public ResponseEntity<List<Expense>> getExpensesByEventId(@PathVariable("id") Long id) {
        return repo.findById(id)
                .map(event -> ResponseEntity.ok(event.getExpenses()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/expenses")
    public ResponseEntity<Event> addExpenseToEvent(@PathVariable("id") Long eventId, @RequestBody Expense expense) {
        return repo.findById(eventId).map(event -> {
            event.addExpense(expense);
            Event updatedEvent = repo.save(event);
            return ResponseEntity.ok(updatedEvent);
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{eventId}/debts")
    public ResponseEntity<Map<Participant, Map<Participant, Integer>>> calculateDebtsForEvent(@PathVariable("eventId") Long eventId) {
        return repo.findById(eventId)
                .map(Event::calculateOptimizedDebts)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
