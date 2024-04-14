package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class AdminCtrlTest {

    @Mock
    private ServerUtils server;
    @Mock
    private MainCtrl mainCtrl;

    @InjectMocks
    private AdminCtrl adminCtrl;

    private ObservableList<Event> data;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminCtrl = new AdminCtrl(server, mainCtrl);
        data = FXCollections.observableArrayList();
        adminCtrl.setData(data);
    }

    @Test
    void testHandleEventUpdateWithExistingEvent() throws InterruptedException {
        Event existingEvent = new Event();
        existingEvent.setId(1L);
        existingEvent.setTitle("Original Event");
        data.add(existingEvent);

        Event updateEvent = new Event();
        updateEvent.setId(1L);
        updateEvent.setTitle("Updated Event");

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            adminCtrl.handleEventUpdate(updateEvent);
            Platform.runLater(latch::countDown);
        });

        assertTrue(latch.await(2, TimeUnit.SECONDS));

        assertEquals("Updated Event", data.get(0).getTitle());
    }

    @Test
    void testHandleEventUpdateWithNewEvent() throws InterruptedException {
        Event newEvent = new Event();
        newEvent.setId(2L);
        newEvent.setTitle("New Event");

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            adminCtrl.handleEventUpdate(newEvent);
            latch.countDown();
        });

        assertTrue(latch.await(2, TimeUnit.SECONDS));

        assertFalse(data.isEmpty());
        assertEquals("New Event", data.get(0).getTitle());
    }
}