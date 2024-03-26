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
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import commons.Event;
import commons.Expense;
import commons.Participant;
import org.glassfish.jersey.client.ClientConfig;

import commons.Quote;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

	private static final String SERVER = "http://localhost:8080/";

	public void getQuotesTheHardWay() throws IOException, URISyntaxException {
		var url = new URI("http://localhost:8080/api/quotes").toURL();
		var is = url.openConnection().getInputStream();
		var br = new BufferedReader(new InputStreamReader(is));
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
	}

	public List<Quote> getQuotes() {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/quotes") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
                .get(new GenericType<List<Quote>>() {});
	}

	public Quote addQuote(Quote quote) {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/quotes") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.post(Entity.entity(quote, APPLICATION_JSON), Quote.class);
	}

//	According to definition a participant can exist only with an event
//	Therefore, there is never a need to create a lone participant
//	public Participant addParticipant(Participant participant) {
//		return ClientBuilder.newClient(new ClientConfig())
//				.target(SERVER).path("api/participant")
//				.request(APPLICATION_JSON)
//				.accept(APPLICATION_JSON)
//				.post(Entity.entity(participant, APPLICATION_JSON), Participant.class);
//	}

	public Event addParticipantToEvent(long eventId, Participant participant) {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/event/" + eventId + "/participants") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.post(Entity.entity(participant, APPLICATION_JSON), Event.class);
	}

	public Event removeParticipantFromEvent(long eventId, long participantId) {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/event/" + eventId + "/participants/" + participantId) //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.delete(Event.class); //
	}

	public Event addEvent(Event event) {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/event") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.post(Entity.entity(event, APPLICATION_JSON), Event.class);
	}

	public Event updateEventTitle(long id, String newTitle) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/event/" + id + "/title")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.put(Entity.text(newTitle), Event.class);
	}

	public Event updateParticipantInEvent(long eventId, Participant updatedParticipant) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/event/" + eventId + "/participants/" + updatedParticipant.getId())
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.put(Entity.entity(updatedParticipant, APPLICATION_JSON), Event.class);
	}

	public List<Event> getEventByInviteCode(String inviteCode) {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/event/invite/" + inviteCode) //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.get(new GenericType<List<Event>>() {});
	}

	public List<Participant> getParticipants() {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/participant") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON)//
				.get(new GenericType<List<Participant>>(){});
	}
	public Expense addExpense(Expense expense) {
		return ClientBuilder.newClient(new ClientConfig())//
				.target(SERVER).path("api/expense")//
				.request(APPLICATION_JSON)//
				.accept(APPLICATION_JSON)//
				.post(Entity.entity(expense, APPLICATION_JSON), Expense.class);
	}

	public void deleteExpense(long id) {
		ClientBuilder.newClient(new ClientConfig())//
				.target(SERVER).path("api/expense?id="+id)//
				.request(APPLICATION_JSON)//
				.accept(APPLICATION_JSON)//
				.delete();
	}
	public Expense putExpense(long id, Expense expense) {
		return ClientBuilder.newClient(new ClientConfig())//
				.target(SERVER).path("api/expense/"+id)//
				.request(APPLICATION_JSON)//
				.accept(APPLICATION_JSON)//
				.put(Entity.entity(expense, APPLICATION_JSON), Expense.class);
	}
}