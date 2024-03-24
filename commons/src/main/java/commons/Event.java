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

    @OneToMany(targetEntity = Participant.class, cascade = CascadeType.ALL)
    private List<Participant> participants;

    @OneToMany(targetEntity = Expense.class, cascade = CascadeType.ALL)
    private List<Expense> expenses;

    public Event() {
        // for object mapper
    }

    public Event(String title) {
        this.title = title;
        this.participants = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.inviteCode = generateInviteCode();
    }

    private String generateInviteCode() {
        return this.id + RandomStringUtils.randomAlphanumeric(4).toUpperCase();
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

    public void addParticipant(Participant participant) {
        participants.add(participant);
    }

    public boolean removeParticipant(Long participantId) {
        return participants.removeIf(participant -> Objects.equals(participant.getId(), participantId));
    }

    public boolean updateParticipant(long participantId, Participant updatedParticipant) {
        for (int i = 0; i < this.participants.size(); i++) {
            Participant current = this.participants.get(i);
            if (current.getId() == participantId) {
                updatedParticipant.setId(participantId);
                this.participants.set(i, updatedParticipant);
                return true;
            }
        }
        return false; // if the participant is not found
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
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

    public boolean checkNull(){
        return title != null;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, title, participants, expenses);
    }

    @Override
    public String toString() {
        String participant = "";
        for(Participant p:participants) participant+=p.getUserName() + " ";
        return "Event " + id +
                ", with the title: " + title +
                ", with the following participants: " + participant +
                ", with the following expenses: " + expenses.toString() + "\n" ;
    }

}
