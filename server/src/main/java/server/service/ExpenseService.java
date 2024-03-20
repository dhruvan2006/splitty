package server.service;

import commons.Expense;
import org.springframework.stereotype.Service;
import server.database.ExpensePayedRepository;
import server.database.ExpensesRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {
    private final ExpensesRepository expensesRepository;
    private final ExpensePayedRepository expensePayedRepository;

    public ExpenseService(ExpensesRepository expensesRepository, ExpensePayedRepository expensePayedRepository) {
        this.expensesRepository = expensesRepository;
        this.expensePayedRepository = expensePayedRepository;
    }

    public List<Expense> getAllExpenses(int page, int size, String sortBy, String sortOrder) {
        // Implement pagination and sorting logic if needed
        return expensesRepository.findAll();
    }

    public Optional<Expense> getExpenseById(Long id) {
        return expensesRepository.findById(id);
    }

    public Expense addExpense(Expense expense) {
        return expensesRepository.save(expense);
    }


    public void deleteExpense(Long id) {
        expensesRepository.deleteById(id);
    }

    public Optional<Expense> updateExpense(Long id, Expense updatedExpense) {
        Optional<Expense> expenseOptional = expensesRepository.findById(id);
        if (expenseOptional.isPresent()) {
            Expense existingExpense = expenseOptional.get();
            existingExpense.setTitle(updatedExpense.getTitle());
            existingExpense.setTotalExpense(updatedExpense.getTotalExpense());

            Expense savedExpense = expensesRepository.save(existingExpense);
            return Optional.of(savedExpense);
        } else {
            return Optional.empty();
        }
    }
}
