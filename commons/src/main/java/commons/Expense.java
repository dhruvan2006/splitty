package commons;

import jakarta.persistence.*;
import java.util.*;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @ManyToOne()
    Participant creator;

    @ManyToOne()
    Event event; // the event this expense belongs to

    @ManyToOne
    List<Participant> participants;

    public Expense() {
    }

    //to indicate if object ready to be added to database
    public boolean isInit(){
        return true;
    }

    private int totalExpense;

    private String title;

    public Expense(String title, int totalExpense, Participant creator) {
        this.title = title;
        this.totalExpense = totalExpense;
        this.creator = creator;
    }

    public Expense(String title, int totalExpense, Participant creator, Event event, List<Participant> participants){
        this.title = title;
        this.totalExpense = totalExpense;
        this.creator = creator;
        this.event = event;
        this.participants = participants;
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

    public List<Participant> getParticipants(){
        return participants;
    }

    public void setCreator(Participant creator) {
        this.creator = creator;
    }

    public void setParticipants(List<Participants> participants){
        this.participants = participants;
    }


    public int getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(int totalExpense) {
        this.totalExpense = totalExpense;
    }

    public int hashCode() {
        return Objects.hash(id, title, creator);//, expenses);
    }

    @Override
    public String toString() {
        return "Expense " + id +
                " - " + title +
                ", created by " + creator.toString() + "\n";
    }

    public int getSharePerPerson(){
        int participantsSize = this.participants.size()-1;
        return getTotalExpense()/participantsSize;
    }
}

