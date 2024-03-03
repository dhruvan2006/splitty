package server.database;

import commons.ExpensePayed;
import commons.ExpensePayedKey;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExpensePayedRepository extends JpaRepository<ExpensePayed, ExpensePayedKey> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ExpensePayed set payed=true where id.participantId = :participantId and id.expenseId = :expenseId")
    void Pay(@Param("participantId") long participantId, @Param("expenseId") long expenseId);
}