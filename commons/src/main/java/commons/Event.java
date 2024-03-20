package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;

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

    public boolean isInit(){
        return title != null && participants.stream().allMatch(Participant::notNull) && expenses.isEmpty();
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, title, participants, expenses);
    }

    @Override
    public String toString() {
        String participant = "";
        for(Participant p:participants) participant+=p.getFullName() + " ";
        return "Event " + id +
                ", with the title: " + title +
                ", with the following participants: " + participant +
                ", with the following expenses: " + expenses.toString() + "\n" ;
    }

    /**
     * showing the total debt owed for this event for each person
     */
    public Map<Participant,Integer> calculateTotalDebt() {
        Map<Participant,Integer> map = new HashMap<>();

        for (Participant participant : participants) {
            Map<Participant, Integer> debts = calculateIndividualDebt().get(participant);
            int totalSum = 0;
            for (Participant other : participants) {

                if (other.equals(participant)) continue;
                totalSum+= debts.getOrDefault(other,0);
            }

            map.put(participant,totalSum);
        }

        return map;
        
    }

    /**
     * does the same, but for each participant within the event
     */
    public Map<Participant, Map<Participant, Integer>> calculateIndividualDebt() {
        Map<Participant, Map<Participant, Integer>> indDebtsMap = new HashMap<>();

        for (Participant participant : participants) {
            indDebtsMap.put(participant, new HashMap<>());
        }

        for (Expense expense : expenses) {
            int sharePerParticipant = expense.getSharePerPerson(participants);

            for (Participant participant : participants) {
                if (participant.equals(expense.getCreator())) continue;

                int currentDebt = indDebtsMap.get(participant).getOrDefault(expense.getCreator(), 0);
                currentDebt += sharePerParticipant;
                indDebtsMap.get(participant).put(expense.getCreator(), currentDebt);
            }
        }

        return indDebtsMap;
    }

    /**
     * showing the total money that a participant is owned
     * could've been made easier by calculating the cost of the expenses a participant has created
     * but this will come in handy when participants will start paying off their debts, so the map
     * will be modified and therefore the total sum will also be modified
     */
    public Map<Participant,Integer> calculateTotalOwned() {
        Map<Participant,Integer> map = new HashMap<>();

        for (Participant participant : participants) {
            Map<Participant, Integer> owned = calculateIndividualOwned().get(participant);
            int totalOwned = 0;
            for (Participant other : participants) {

                if (other.equals(participant)) continue;
                totalOwned+= owned.getOrDefault(other,0);
            }

            map.put(participant,totalOwned);
        }

        return map;
        
    }


    /**
    * the same thing as above but a map so a participant can see who owes him how much within an event
    */
    public Map<Participant, Map<Participant, Integer>> calculateIndividualOwned() {
        Map<Participant, Map<Participant, Integer>> ownedMap = new HashMap<>();

        for (Participant participant : participants) {
            ownedMap.put(participant, new HashMap<>());
        }

        for (Expense expense : expenses) {
            int sharePerParticipant = expense.getSharePerPerson(participants);

            for (Participant other : participants) {
                if (other.equals(expense.getCreator())) continue; 

                int currentOwned = ownedMap.get(expense.getCreator()).getOrDefault(other, 0);
                currentOwned += sharePerParticipant;
                ownedMap.get(expense.getCreator()).put(other, currentOwned);
            }
        }

        return ownedMap;
    }


}
