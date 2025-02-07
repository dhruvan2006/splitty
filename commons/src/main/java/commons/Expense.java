package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.*;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @ManyToOne()
    Participant creator;

    public void setEvent(Event event) {
        this.event = event;
    }

    @ManyToOne()
    @JsonBackReference
    Event event; // the event this expense belongs to

    public Expense() {
    }

    //to indicate if object ready to be added to database
    public boolean checkNull(){
        return true;
    }

    private int totalExpense;

    private String title;

    public Expense(String title, int totalExpense, Participant creator) {
        this.title = title;
        this.totalExpense = totalExpense;
        this.creator = creator;
    }

    public Expense(String title, int totalExpense, Participant creator, Event event){
        this.title = title;
        this.totalExpense = totalExpense;
        this.creator = creator;
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Participant getCreator() {
        return creator;
    }

    public void setCreator(Participant creator) {
        this.creator = creator;
    }

    public int getTotalExpense() {
        return totalExpense;
    }

    public String getTotalExpenseString(){
        return String.format(java.util.Locale.US,"%.2f", totalExpense / 100f);
    }

    public void setTotalExpense(int totalExpense) {
        this.totalExpense = totalExpense;
    }

    public int hashCode() {
        return Objects.hash(id, title, creator);//, expenses);
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", creator=" + creator +
                ", event=" + event +
                ", totalExpense=" + totalExpense +
                ", title='" + title + '\'' +
                '}';
    }

    public int getSharePerPerson(int amountParticipants){
        return getTotalExpense() == 0 ? 0 : amountParticipants == 0 ? getTotalExpense() : getTotalExpense()/amountParticipants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Expense expense)) return false;
        return getId() == expense.getId() && getTotalExpense() == expense.getTotalExpense() && Objects.equals(getCreator(), expense.getCreator()) && Objects.equals(getEvent(), expense.getEvent()) && Objects.equals(getTitle(), expense.getTitle());
    }
}