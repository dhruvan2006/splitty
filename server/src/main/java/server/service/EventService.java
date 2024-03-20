package server.service;

import commons.Event;
import org.springframework.stereotype.Service;
import server.database.EventRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents(int page, int size, String sortBy, String sortOrder) {
        // Implement pagination and sorting logic if needed
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public Event createEvent(Event event) {
        if (event.checkNull()) {
            // Perform validation or additional logic if needed
            return eventRepository.save(event);
        } else {
            // Throw an exception or handle invalid event creation
            throw new IllegalArgumentException("Invalid event data");
        }
    }

    public void deleteEventByID(Long id) {
        eventRepository.deleteById(id);
    }
}
