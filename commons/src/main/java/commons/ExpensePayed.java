package commons;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class ExpensePayed {
    @EmbeddedId
    ExpensePayedKey id;

    @ManyToOne
    @MapsId("expenseId")
    @JoinColumn(name = "expense_id")
    Expense expense;

    @ManyToOne
    @MapsId("participantId")
    @JoinColumn(name = "participant_id")
    Participant participant;

    boolean payed = false;

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
