/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.notNullValue;


import commons.Event;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;



public class EventControllerTest {


    @Test
    public void cannotBeNull() {
        Event event = new Event("New Year");
        List<Event> events = new ArrayList<>();
        events.add(event);
        TestEventRepository repo = new TestEventRepository(events);
        EventController sut = new EventController(repo);
        var actual = sut.createEvent(null);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }
    @Test
    public void cannotBeNull1() {
        Event event = new Event("New Year");
        Event event1 = new Event(null);
        List<Event> events = new ArrayList<>();
        events.add(event);
        TestEventRepository repo = new TestEventRepository(events);
        EventController sut = new EventController(repo);
        var actual = sut.createEvent(event1);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }
    @Test
    public void validEvent() {
        Event event = new Event("New Year");
        Event event1 = new Event("Old Year");
        List<Event> events = new ArrayList<>();
        events.add(event);
        TestEventRepository repo = new TestEventRepository(events);
        EventController sut = new EventController(repo);
        var actual = sut.createEvent(event1);
        assertEquals(OK, actual.getStatusCode());
    }
    @Test
    public void postContentTest() {
        Event event = new Event("New Year");
        Event event1 = new Event("Old Year");
        List<Event> events = new ArrayList<>();
        events.add(event);
        TestEventRepository repo = new TestEventRepository(events);
        EventController sut = new EventController(repo);
        var actual = sut.createEvent(event1);
        assertEquals(event1, actual.getBody());
    }

//    @Test
//    public void databaseIsUsed() {
//        Event event = new Event("New Year");
//        Event event1 = new Event("Old Year");
//        List<Event> events = new ArrayList<>();
//        events.add(event);
//        TestEventRepository repo = new TestEventRepository(events);
//        EventController sut = new EventController(repo);
//        var actual = sut.postEvent(event1);
//        assertEquals(OK, actual.getStatusCode());
//        repo.calledMethods.contains("save");
//    }
    @Test
    public void getTest() {
        Event event = new Event("New Year");
        Event event1 = new Event("Old Year");
        List<Event> events = new ArrayList<>();
        events.add(event);
        TestEventRepository repo = new TestEventRepository(events);
        EventController sut = new EventController(repo);
        var actual = sut.createEvent(event1);
        assertEquals(OK, actual.getStatusCode());
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void testHandleEventUpdate() throws InterruptedException {
        Event event = new Event("New Year");
        Event event1 = new Event("Old Year");
        List<Event> events = new ArrayList<>();
        events.add(event);
        TestEventRepository repo = new TestEventRepository(events);
        EventController eventController = new EventController(repo);

        Long eventId = event.getId();
        DeferredResult<ResponseEntity<Event>> deferredResult = eventController.getEventUpdates(eventId);

        long startTime = System.currentTimeMillis();
        long timeoutInMillis = 5000; 
        ResponseEntity<Event> responseEntity = null;

        //Poll every 100 milliseconds
        while (System.currentTimeMillis() - startTime < timeoutInMillis) {
            responseEntity = deferredResult.getResult();
            if (responseEntity != null) {
                break; 
            }
            Thread.sleep(100); 
        }

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(event1, responseEntity.getBody());
    }

    @Test
    void testGetEventUpdatesTimeout() {
        Event event = new Event("New Year");
        List<Event> events = new ArrayList<>();
        events.add(event);
        TestEventRepository repo = new TestEventRepository(events);
        EventController eventController = new EventController(repo);

        Long eventId = event.getId();
        DeferredResult<ResponseEntity<Event>> deferredResult = eventController.getEventUpdates(eventId);

        TimeUnit.SECONDS.sleep(30);

        ResponseEntity<Event> responseEntity = deferredResult.getResult();
        assertNull(responseEntity);
        assertEquals(HttpStatus.REQUEST_TIMEOUT, deferredResult.getResult().getStatusCode());
    }

    @Test
    public void testHandleEventUpdateWithInvalidEventId() {
        EventController eventController = new EventController(new TestEventRepository());
        
        Long invalidEventId = -1L;
        Event event = new Event("New Year");
        eventController.handleEventUpdate(invalidEventId, event);

        DeferredResult<ResponseEntity<Event>> deferredResult = eventController.getDeferredResult(invalidEventId);
        assertNull(deferredResult);
    }
    
}