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


import commons.Participant;
import org.junit.jupiter.api.Test;

public class ParticipantControllerTest {


    @Test
    public void cannotAddNullPerson() {
        TestParticipantRepository repo = new TestParticipantRepository();
        ParticipantController sut = new ParticipantController(repo);
        Participant participant = new Participant("x", "y", "z", "123", "w");
        var actual = sut.postParticipant(null);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }
    @Test
    public void cannotAddNullPerson1() {
        TestParticipantRepository repo = new TestParticipantRepository();
        ParticipantController sut = new ParticipantController(repo);
        Participant participant = new Participant(null, null, null, null, null);
        var actual = sut.postParticipant(participant);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }
    @Test
    public void validPerson() {
        TestParticipantRepository repo = new TestParticipantRepository();
        ParticipantController sut = new ParticipantController(repo);
        Participant participant = new Participant("x", "y", "z", "123", "w");
        var actual = sut.postParticipant(participant);
        assertEquals(OK, actual.getStatusCode());
    }
    @Test
    public void postContentTest() {
        TestParticipantRepository repo = new TestParticipantRepository();
        ParticipantController sut = new ParticipantController(repo);
        Participant participant = new Participant("x", "y", "z", "123", "w");
        var actual = sut.postParticipant(participant);
        assertEquals(participant, actual.getBody());
    }

    @Test
    public void databaseIsUsed() {
        TestParticipantRepository repo = new TestParticipantRepository();
        ParticipantController sut = new ParticipantController(repo);
        Participant participant = new Participant("x", "y", "z", "123", "w");
        sut.postParticipant(participant);
        repo.calledMethods.contains("save");
    }
    @Test
    public void getTest() {
        TestParticipantRepository repo = new TestParticipantRepository();
        ParticipantController sut = new ParticipantController(repo);
        Participant participant = new Participant("x", "y", "z", "123", "w");
        var actual = sut.findAllParticipants();
        assertEquals(OK, actual.getStatusCode());
    }

    @Test
    public void deleteTest() {
        TestParticipantRepository repo = new TestParticipantRepository();
        ParticipantController sut = new ParticipantController(repo);
        Participant participant = new Participant("x", "y", "z", "123", "w");
        Participant posted = sut.postParticipant(participant).getBody();
        assert posted != null;
        var actual = sut.deleteIt(posted.getId());
        assertEquals(OK, actual.getStatusCode());
    }
}