package commons;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ExpensePayedKey implements Serializable {
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
