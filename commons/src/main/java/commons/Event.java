package commons;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String inviteCode;
    private String title;

    private Timestamp openDate;
    private Timestamp lastUsed;


    @OneToMany(targetEntity = Participant.class, cascade = CascadeType.ALL)
    private List<Participant> participants;

    @OneToMany(targetEntity = Expense.class, cascade = CascadeType.ALL, mappedBy = "event")
    @JsonManagedReference
    private List<Expense> expenses;

    public Event() {
        // for object mapper
    }

    public Event(String title, boolean ...useMockData) {
        this.title = title;
        this.participants = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.inviteCode = generateInviteCode();
        this.openDate = Timestamp.valueOf(LocalDateTime.now());
        this.lastUsed = Timestamp.valueOf(LocalDateTime.now());
    }

    private String generateInviteCode() {
        return RandomStringUtils.randomAlphanumeric(6).toUpperCase();
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

    public Timestamp getOpenDate() {
        return openDate;
    }

    public Timestamp getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Timestamp lastUsed) {
        this.lastUsed = lastUsed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id;
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
        return title +
                " (code: "
                + inviteCode +
                ")";
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
            int sharePerParticipant = expense.getSharePerPerson(participants.size());

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
     * Calculates the individual debts and then cancels out any debts that two participants owe each other
     * @return the optimized debt map
     */
    public Map<Participant, Map<Participant, Integer>> calculateOptimizedDebts() {
        Map<Participant, Map<Participant, Integer>> individualDebts = calculateIndividualDebt();
        Map<Participant, Map<Participant, Integer>> optimizedDebts = new HashMap<>();

        // First, cancel out any debts that cancel each other out
        for (Map.Entry<Participant, Map<Participant, Integer>> entry : individualDebts.entrySet()) {
            Participant participant = entry.getKey();
            Map<Participant, Integer> debts = entry.getValue();
            optimizedDebts.put(participant, new HashMap<>(debts));

            for (Map.Entry<Participant, Integer> debtEntry : debts.entrySet()) {
                Participant other = debtEntry.getKey();
                int debt = debtEntry.getValue();

                if (debts.containsKey(other) && individualDebts.get(other) != null && individualDebts.get(other).containsKey(participant)) {
                    int otherDebt = individualDebts.get(other).get(participant);
                    if (debt == otherDebt) {
                        optimizedDebts.get(participant).remove(other);
                        optimizedDebts.computeIfPresent(other, (p, otherDebts) -> {
                            otherDebts.remove(participant);
                            return otherDebts;
                        });
                    } else if (debt < otherDebt) {
                        optimizedDebts.get(participant).remove(other);
                        optimizedDebts.computeIfPresent(other, (p, otherDebts) -> {
                            otherDebts.put(participant, otherDebt - debt);
                            return otherDebts;
                        });
                    } else {
                        optimizedDebts.get(participant).put(other, debt - otherDebt);
                        optimizedDebts.computeIfPresent(other, (p, otherDebts) -> {
                            otherDebts.remove(participant);
                            return otherDebts;
                        });
                    }
                }
            }
        }

        return optimizedDebts;
    }
}
