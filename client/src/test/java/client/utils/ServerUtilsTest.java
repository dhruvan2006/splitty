package client.utils;

import client.utils.ServerUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.*;
import org.junit.jupiter.api.*;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.JsonBody;
import org.mockserver.verify.VerificationTimes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockserver.model.JsonBody.json;
import static org.mockserver.verify.VerificationTimes.once;

public class ServerUtilsTest {
    private static final int MOCK_SERVER_PORT = 8080;
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


}