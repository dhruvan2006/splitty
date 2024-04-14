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

import static jakarta.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.function.Consumer;

import commons.Event;
import commons.Expense;
import commons.Participant;
import client.config.Configuration;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import commons.Quote;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class ServerUtils {

	private static final String SERVER = Configuration.getInstance().getServerUrl();
	private StompSession session = connect("ws://localhost:8080/websocket");
	private StompSession connect(String url){
		var client = new StandardWebSocketClient();
		var stomp = new WebSocketStompClient(client);
		stomp.setMessageConverter(new MappingJackson2MessageConverter());
		try {
			return stomp.connectAsync(url, new StompSessionHandler() {
				@Override
				public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
				}

				@Override
				public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {

				}

				@Override
				public void handleTransportError(StompSession session, Throwable exception) {

				}

				@Override
				public Type getPayloadType(StompHeaders headers) {
					return null;
				}

				@Override
				public void handleFrame(StompHeaders headers, Object payload) {

				}
			}).get();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	//TO FINISH
	public <T> void registerForMessages(String dest, Class<T> type, Consumer<T> consumer) {
		session.subscribe(dest, new StompFrameHandler() {
			@Override
			public Type getPayloadType(StompHeaders headers) {
				return type;
			}

			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
				consumer.accept((T) payload);
			}
		});
	}

	public void send(String dest, Object o){
		session.send(dest, o);
	}
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

	public List<Event> getEvents() {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("/api/event") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.get(new GenericType<List<Event>>() {});
	}

	public Event addEvent(Event event) {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/event") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.post(Entity.entity(event, APPLICATION_JSON), Event.class);
	}

	public boolean deleteEventById(long eventId) {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/event") //
				.queryParam("id", eventId)
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.delete()
				.getStatus() == Response.Status.NO_CONTENT.getStatusCode();
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
				.target(SERVER).path("api/expense/addToEvent/" + expense.getEvent().getId())//
				.request(APPLICATION_JSON)//
				.accept(APPLICATION_JSON)//
				.post(Entity.entity(expense, APPLICATION_JSON), Expense.class);
	}

	public void deleteExpense(long id) {
		ClientBuilder.newClient(new ClientConfig())//
				.target(SERVER).path("api/expense/" + id)//
				.request(APPLICATION_JSON)//
				.accept(APPLICATION_JSON)//
				.delete(Expense.class);
	}
	public Expense putExpense(long id, Expense expense) {
		return ClientBuilder.newClient(new ClientConfig())//
				.target(SERVER).path("api/expense/"+id)//
				.request(APPLICATION_JSON)//
				.accept(APPLICATION_JSON)//
				.put(Entity.entity(expense, APPLICATION_JSON), Expense.class);
	}

	public boolean authenticateAdmin(String password) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("admin/authenticate")
				.request(APPLICATION_FORM_URLENCODED)
				.accept(APPLICATION_JSON)
				.post(Entity.entity(new Form().param("password", password), APPLICATION_FORM_URLENCODED), Boolean.class);
	}

	public Event updateLastUsedDate(Long id){
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/event/" + id + "/date")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.put(Entity.text(""), Event.class);
	}
}