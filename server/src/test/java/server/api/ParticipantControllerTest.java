package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import commons.Participant;
import org.junit.jupiter.api.Test;

public class ParticipantControllerTest {

    @Test
    public void cannotAddNullPerson() {
        TestParticipantRepository repo = new TestParticipantRepository();
        ParticipantController sut = new ParticipantController(repo);
        var actual = sut.createParticipant(null);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void cannotAddNullPerson1() {
        TestParticipantRepository repo = new TestParticipantRepository();
        ParticipantController sut = new ParticipantController(repo);
        Participant participant = new Participant(null, null, null);
        var actual = sut.createParticipant(participant);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void validPerson() {
        TestParticipantRepository repo = new TestParticipantRepository();
        ParticipantController sut = new ParticipantController(repo);
        Participant participant = new Participant("x", "y", "z");
        var actual = sut.createParticipant(participant);
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void postContentTest() {
        TestParticipantRepository repo = new TestParticipantRepository();
        ParticipantController sut = new ParticipantController(repo);
        Participant participant = new Participant("x", "y", "z");
        var actual = sut.createParticipant(participant);
        assertEquals(participant, actual.getBody());
    }

    @Test
    public void databaseIsUsed() {
        TestParticipantRepository repo = new TestParticipantRepository();
        ParticipantController sut = new ParticipantController(repo);
        Participant participant = new Participant("x", "y", "z");
        sut.createParticipant(participant);
        assertEquals(true, repo.calledMethods.contains("save"));
    }

    @Test
    public void getTest() {
        TestParticipantRepository repo = new TestParticipantRepository();
        ParticipantController sut = new ParticipantController(repo);
        var actual = sut.findAllParticipants();
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void deleteTest() {
        TestParticipantRepository repo = new TestParticipantRepository();
        ParticipantController sut = new ParticipantController(repo);
        Participant participant = new Participant("x", "y", "z");
        Participant posted = sut.createParticipant(participant).getBody();
        assert posted != null;
        var actual = sut.deleteIt(posted.getId());
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void getById_ValidId_ReturnsParticipant() {
        TestParticipantRepository repo = new TestParticipantRepository();
        ParticipantController sut = new ParticipantController(repo);
        Participant participant = new Participant("John", "Doe", "jd@g.com");
        Participant savedParticipant = sut.createParticipant(participant).getBody();
        var actual = sut.getById(String.valueOf(savedParticipant.getId()));
        assertEquals(OK, actual.getStatusCode());
        assertEquals(savedParticipant, actual.getBody().get(0));
    }

    @Test
    public void getById_InexistentId_ReturnsBadRequest() {
        TestParticipantRepository repo = new TestParticipantRepository();
        ParticipantController sut = new ParticipantController(repo);
        var actual = sut.getById("9999"); 
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void getById_InvalidId_ReturnsBadRequest() {
        TestParticipantRepository repo = new TestParticipantRepository();
        ParticipantController sut = new ParticipantController(repo);
        var actual = sut.getById("abc"); 
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

}
