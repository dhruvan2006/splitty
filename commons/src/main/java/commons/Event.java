package commons;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    //I think invite code is a same as id
    //private String inviteCode;
    private String title;

    @OneToMany(targetEntity = Participant.class)
    private List<Participant> participants;

    @OneToMany(targetEntity = Expense.class)
    private List<Expense> expenses;

    private Event() {
        // for object mapper
    }

    public Event(String title) {
        this.title = title;
        this.participants = new ArrayList<>();
        this.expenses = new ArrayList<>();
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {this.id = id;}


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
                && Objects.equals(title, event.title)
                && Objects.equals(participants, event.participants)
                && Objects.equals(expenses, event.expenses);
    }

    public boolean isInit(){
        return title != null && participants.stream().allMatch(Participant::notNull) && expenses.isEmpty();
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, title, participants);//, expenses);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", inviteCode='" + '\'' +
                ", title='" + title + '\'' +
                ", participants=" + participants +
                ", expenses=" + expenses +
                '}';
    }

}
