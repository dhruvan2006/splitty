package client.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.*;
import org.junit.jupiter.api.*;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.JsonBody;

import java.util.List;
import static org.mockserver.verify.VerificationTimes.once;

import org.junit.jupiter.api.function.Executable;

public class ServerUtilsTest {
    private static final int MOCK_SERVER_PORT = 1234;
    private static ClientAndServer mockServer;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private ServerUtils serverUtils;

    @BeforeAll
    public static void openConnection() {
        mockServer = ClientAndServer.startClientAndServer(MOCK_SERVER_PORT);
    }

    @AfterAll
    public static void closeConnection() {
        mockServer.stop();
    }

    @BeforeEach
    public void setUp() {
        serverUtils = new ServerUtils();
        serverUtils.setSERVER("http://localhost:"+ MOCK_SERVER_PORT+ "/");
    }

    @AfterEach
    public void tearDown() {
        mockServer.reset();
    }

    @Test
    public void testGetEvent() throws JsonProcessingException {

        List<Event> expectedEvents = List.of(
                new Event("E1"),
                new Event("E2"),
                new Event("E3")
        );
        mockServer.when(HttpRequest.request().withMethod("GET").withPath("/api/event"))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(expectedEvents)));
        List<Event> actualEvents = serverUtils.getEvents();
        Assertions.assertEquals(expectedEvents, actualEvents);
    }

    @Test
    public void  testGetEventByID() throws JsonProcessingException {
        Event x = new Event("E1");
        List<Event> expectedEvent = List.of(x);
        mockServer.when(HttpRequest.request().withMethod("GET").withPath("/api/event/invite/" + x.getInviteCode()))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(expectedEvent)));
        List<Event> actualEvent = serverUtils.getEventByInviteCode(x.getInviteCode());
        Assertions.assertEquals(expectedEvent, actualEvent);
    }

    @Test
    public void testAddEvent() throws JsonProcessingException {
        Event expectedEvent = new Event("New Event");
        mockServer.when(HttpRequest.request().withMethod("POST").withPath("/api/event"))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(expectedEvent)));
        Event actualEvent = serverUtils.addEvent(expectedEvent);
        Assertions.assertEquals(expectedEvent, actualEvent);
    }


    @Test
    public void testDeleteEventById() {
        long eventId = 1L;
        mockServer.when(HttpRequest.request().withMethod("DELETE").withPath("/api/event").withQueryStringParameter("id", String.valueOf(eventId)))
                .respond(HttpResponse.response().withStatusCode(204));
        boolean result = serverUtils.deleteEventById(eventId);
        Assertions.assertTrue(result);
    }

    @Test
    public void testUpdateEventTitle() throws JsonProcessingException {
        long eventId = 1L;
        String newTitle = "Updated Title";
        Event expectedEvent = new Event("Updated Title");
        mockServer.when(HttpRequest.request().withMethod("PUT").withPath("/api/event/" + eventId + "/title"))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(expectedEvent)));
        Event actualEvent = serverUtils.updateEventTitle(eventId, newTitle);
        Assertions.assertEquals(expectedEvent, actualEvent);
    }

    @Test
    public void testUpdateParticipantInEvent() throws JsonProcessingException {
        long eventId = 1L;
        Participant updatedParticipant = new Participant("john@example.com", "IBAN12345", "JD");
        Event expectedEvent = new Event("Event with Updated Participant");
        mockServer.when(HttpRequest.request().withMethod("PUT").withPath("/api/event/" + eventId + "/participants/" + updatedParticipant.getId()))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(expectedEvent)));
        Event actualEvent = serverUtils.updateParticipantInEvent(eventId, updatedParticipant);
        Assertions.assertEquals(expectedEvent, actualEvent);
    }

    @Test
    public void testAddParticipantToEvent() throws JsonProcessingException {
        Event event = new Event("Event with New Participant");
        Participant participant = new Participant("email@example.com", "IBAN123", "username");
        Event expectedEvent = new Event("Event with New Participant");
        mockServer.when(HttpRequest.request().withMethod("POST").withPath("/api/event/" + event.getId() + "/participants"))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(expectedEvent)));
        Event actualEvent = serverUtils.addParticipantToEvent(event.getId(), participant);
        Assertions.assertEquals(expectedEvent, actualEvent);
    }


    @Test
    public void testRemoveParticipantFromEvent() throws JsonProcessingException {
        Event event = new Event("Event without Participant");
        Participant participant = new Participant("email@example.com", "IBAN123", "username");
        mockServer.when(HttpRequest.request().withMethod("DELETE").withPath("/api/event/" + event.getId() + "/participants/" + participant.getId()))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(event)));
        Event actualEvent = serverUtils.removeParticipantFromEvent(event.getId(), participant.getId());
        Assertions.assertEquals(event, actualEvent);
    }

    @Test
    public void testAddExpense() throws JsonProcessingException {
        Event event = new Event("Event with New Expense");
        Participant participant = new Participant("email@example.com", "IBAN123", "username");
        Expense expectedExpense = new Expense("Expense Title", 100, participant, event);
        mockServer.when(HttpRequest.request().withMethod("POST").withPath("/api/expense/addToEvent/" + event.getId()))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(expectedExpense)));
        Expense actualExpense = serverUtils.addExpense(expectedExpense);
        actualExpense.setEvent(event);
        Assertions.assertEquals(expectedExpense, actualExpense);
    }


    @Test
    public void testDeleteExpense() {
        Expense expense = new Expense("Expense 1", 100, new Participant("email@example.com", "IBAN123", "username"));
        mockServer.when(HttpRequest.request().withMethod("DELETE").withPath("/api/expense/" + expense.getId()))
                .respond(HttpResponse.response().withStatusCode(204));
        serverUtils.deleteExpense(expense.getId());
        mockServer.verify(HttpRequest.request().withMethod("DELETE").withPath("/api/expense/" + expense.getId()), once());
    }

    @Test
    public void testPutExpense() throws JsonProcessingException {
        Event event = new Event("Event with New Expense");
        Participant participant = new Participant("email@example.com", "IBAN123", "username");
        Expense expectedExpense = new Expense("Expense Title", 100, participant);
        mockServer.when(HttpRequest.request().withMethod("PUT").withPath("/api/expense/" + event.getId()))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(expectedExpense)));
        Expense actualExpense = serverUtils.putExpense(event.getId(), expectedExpense);
        Assertions.assertEquals(expectedExpense, actualExpense);
    }


    @Test
    public void testUpdateLastUsedDate() throws JsonProcessingException {
        Long eventId = 1L;
        Event expectedEvent = new Event("Event with Updated Date");
        mockServer.when(HttpRequest.request().withMethod("PUT").withPath("/api/event/" + eventId + "/date"))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(expectedEvent)));
        Event actualEvent = serverUtils.updateLastUsedDate(eventId);
        Assertions.assertEquals(expectedEvent, actualEvent);
    }

    
    @Test
    public void testAuthenticateAdmin() {
        String password = "adminPassword";
        mockServer.when(HttpRequest.request().withMethod("POST").withPath("/admin/authenticate")
                .withBody(JsonBody.json("{ \"password\": \"" + password + "\" }"), 1))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("true"));

        boolean result = serverUtils.authenticateAdmin(password);
        Assertions.assertTrue(result);
    }

    @Test
    public void testGivesException() {
        String password = "adminPassword";
        mockServer.when(HttpRequest.request().withMethod("POST").withPath("/admin/authenticate")
                .withBody(JsonBody.json("{ \"password\": \"" + password + "\" }"), 1))
                .respond(HttpResponse.response()
                        .withStatusCode(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody("Internal Server Error"));

        Assertions.assertThrows(Exception.class, () -> {
            serverUtils.authenticateAdmin(password);
        });
    }





}