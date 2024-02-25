package commons;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @OneToMany(mappedBy = "Participant")
    Set<ExpensePayed> expensesPayed;

    @ManyToOne()
    Participant collector;

    private String title;

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Participant getCollector() {
        return collector;
    }

    private Set<ExpensePayed> getExpensesPayed() {
        return expensesPayed;
    }

    public Set<Participant> getParticipantsWhoPayed(){
        return expensesPayed.stream().filter((expensePayed -> expensePayed.payed))
                .map((expensePayed -> expensePayed.participant))
                .collect(Collectors.toSet());
    }

    public Set<Participant> getParticipantsWhoNotPayed(){
        return expensesPayed.stream().filter((expensePayed -> !expensePayed.payed))
                .map((expensePayed -> expensePayed.participant))
                .collect(Collectors.toSet());
    }

    public int hashCode() {
        return Objects.hash(id, title, collector);//, expenses);
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", title='" + title + '\'' +
                "created by" + collector + '\'' +
                '}';
    }
}

@Entity
class ExpensePayed {
    @EmbeddedId
    ExpensePayedKey id;

    @ManyToOne
    @MapsId("participantId")
    @JoinColumn(name = "expense_id")
    Expense expense;

    @ManyToOne
    @MapsId("expenseId")
    @JoinColumn(name = "participant_id")
    Participant participant;

    boolean payed;

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    public ExpensePayedKey getId() {
        return id;
    }

    public Expense getExpense() {
        return expense;
    }

    public Participant getParticipant() {
        return participant;
    }
}

@Embeddable
class ExpensePayedKey implements Serializable {
    @Column(name = "expense_id")
    Long expenseId;

    @Column(name = "participant_id")
    Long participantId;

    public Long getExpenseId() {
        return expenseId;
    }

    public Long getParticipantId() {
        return participantId;
    }
}
