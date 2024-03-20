package server.database;

import commons.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpensePayedRepository extends JpaRepository<Expense, Long> { }