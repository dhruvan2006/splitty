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

    private String title;

    @OneToMany(targetEntity = Participant.class)
    private List<Participant> participants;

//    @OneToMany(targetEntity = Expense.class)
//    private List<Expense> expenses;

    private Event() {
        // for object mapper
    }

    public Event(String title) {
        this.title = title;
        this.participants = new ArrayList<>();
//        this.expenses = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id
                && Objects.equals(title, event.title)
                && Objects.equals(participants, event.participants);
//                && Objects.equals(expenses, event.expenses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, participants);//, expenses);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", participants=" + participants +
//                ", expenses=" + expenses +
                '}';
    }
}
