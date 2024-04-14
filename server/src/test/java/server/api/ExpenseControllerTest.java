package server.api;

import commons.Event;
import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.ExpensesRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
class ExpenseControllerTest {

    @Mock
    private ExpensesRepository expensesRepository;

    @InjectMocks
    private ExpenseController expenseController;

    private Event event;
    private Participant participant;
    private Expense expense;


    @BeforeEach
    void setUp() {
        event = new Event();
        event.setId(1L);

        participant = new Participant();
        participant.setId(1L);

        expense = new Expense();
        expense.setId(1L);
        expense.setEvent(event);
        expense.setCreator(participant);
    }


    @Test
    void testAddExpense() {
        when(expensesRepository.save(any(Expense.class))).thenReturn(expense);

        ResponseEntity<Expense> response = expenseController.add(expense, 1L);

        assertEquals(OK, response.getStatusCode());
        assertEquals(expense, response.getBody());
    }


    @Test
    public void testDeleteExpense_ValidId() {
        when(expensesRepository.existsById(1L)).thenReturn(true);
        doNothing().when(expensesRepository).deleteById(1L);

        ResponseEntity<Expense> response = expenseController.deleteIt(1L);
        assertEquals(OK, response.getStatusCode());
    }

    @Test
    public void testDeleteExpense_InvalidId() {
        when(expensesRepository.existsById(anyLong())).thenReturn(false);

        ResponseEntity<Expense> response = expenseController.deleteIt(99L);

        assertEquals(BAD_REQUEST, response.getStatusCode());
    }


    @Test
    public void testUpdateExpense_Found() {
        Expense updatedExpense = new Expense();
        updatedExpense.setTitle("Updated Expense");
        updatedExpense.setTotalExpense(1369);

        when(expensesRepository.findById(1L)).thenReturn(Optional.of(expense));
        when(expensesRepository.save(any(Expense.class))).thenReturn(expense);

        ResponseEntity<Expense> response = expenseController.updateExpense(1L, updatedExpense);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Expense", response.getBody().getTitle());
        assertEquals(1369, response.getBody().getTotalExpense());
    }

    @Test
    public void testUpdateExpense_NotFound() {
        when(expensesRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Expense> response = expenseController.updateExpense(1L, new Expense());

        assertEquals(NOT_FOUND, response.getStatusCode());
    }
}