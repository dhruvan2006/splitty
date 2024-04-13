package server.api;

import commons.Event;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.EventRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventController eventController;

    private Event event;
    private Participant participant;


    @BeforeEach
    void setUp() {
        event = new Event("Sample Event");
        event.setId(1L);
        participant = new Participant();
        participant.setId(1L);
        event.addParticipant(participant);
    }


    @Test
    void testGetAllEvents() {
        when(eventRepository.findAll()).thenReturn(Arrays.asList(event));
        ResponseEntity<List<Event>> response = eventController.getAllEvents();
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(eventRepository).findAll();
    }


    @Test
    void testValidGetByInviteCode() {
        String inviteCode = "ABCDEF";
        event.setInviteCode(inviteCode);
        when(eventRepository.findAll()).thenReturn(Arrays.asList(event));

        ResponseEntity<List<Event>> response = eventController.getByInviteCode(inviteCode);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
        assertEquals(1, response.getBody().size());
        assertEquals(event, response.getBody().get(0));
        verify(eventRepository).findAll();
    }

    @Test
    void testInvalidGetByInviteCode() {
        ResponseEntity<List<Event>> response = eventController.getByInviteCode("");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(eventRepository, never()).findAll();
    }


    @Test
    void testValidUpdateEventTitle() {
        String newTitle = "New Title";
        long eventId = event.getId();
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        ResponseEntity<Event> response = eventController.updateEventTitle(eventId, newTitle);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(newTitle, response.getBody().getTitle());
        verify(eventRepository).findById(eventId);
        verify(eventRepository).save(event);
    }

    @Test
    void testEmptyUpdateEventTitle() {
        ResponseEntity<Event> response = eventController.updateEventTitle(1L, "  ");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(eventRepository, never()).findById(1L);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void testNotFoundUpdateEventTitle() {
        long eventId = 2L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        ResponseEntity<Event> response = eventController.updateEventTitle(eventId, "New Title");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(eventRepository).findById(eventId);
        verify(eventRepository, never()).save(any(Event.class));
    }


    @Test
    void testValidAddParticipantToEvent() {
        Participant participant1 = new Participant();
        participant1.setId(2L);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(event)).thenReturn(event);

        assertFalse(event.getParticipants().contains(participant1));

        ResponseEntity<Event> response = eventController.addParticipantToEvent(1L, participant1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        assertTrue(event.getParticipants().contains(participant1));

        verify(eventRepository).findById(1L);
        verify(eventRepository).save(event);
    }

    @Test
    void testInvalidAddParticipantToEvent() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Event> response = eventController.addParticipantToEvent(1L, participant);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(eventRepository).findById(1L);
        verify(eventRepository, never()).save(event);
    }


    @Test
    void testValidRemoveParticipantFromEvent() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        ResponseEntity<Event> response = eventController.removeParticipantFromEvent(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(event.getParticipants().contains(participant));
        verify(eventRepository).findById(1L);
        verify(eventRepository).save(event);
    }

    @Test
    void testRemoveParticipantFromEvent_ParticipantNotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        ResponseEntity<Event> response = eventController.removeParticipantFromEvent(1L, 3L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(event.getParticipants().contains(participant));
        verify(eventRepository).findById(1L);
        verify(eventRepository, never()).save(event);
    }

    @Test
    void testRemoveParticipantFromEvent_EventNotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Event> response = eventController.removeParticipantFromEvent(1L, 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(eventRepository).findById(1L);
        verify(eventRepository, never()).save(any(Event.class));
    }
}