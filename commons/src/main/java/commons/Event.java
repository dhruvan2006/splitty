package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String inviteCode;
    private String title;

    @OneToMany(targetEntity = Participant.class)
    private List<Participant> participants;

    @OneToMany(targetEntity = Expense.class)
    private List<Expense> expenses;

    private Event() {
        // for object mapper
    }

    public Event(String title) {
        this.inviteCode = generateInviteCode();
        this.title = title;
        this.participants = new ArrayList<>();
        this.expenses = new ArrayList<>();
    }

    private String generateInviteCode() {
        return id + RandomStringUtils.randomAlphanumeric(5).toUpperCase();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id
                && Objects.equals(inviteCode, event.inviteCode)
                && Objects.equals(title, event.title)
                && Objects.equals(participants, event.participants)
                && Objects.equals(expenses, event.expenses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, inviteCode, title, participants, expenses);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", inviteCode='" + inviteCode + '\'' +
                ", title='" + title + '\'' +
                ", participants=" + participants +
                ", expenses=" + expenses +
                '}';
    }
}
