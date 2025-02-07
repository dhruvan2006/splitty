package server.api;

import commons.Event;
import commons.Expense;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import server.database.ExpensesRepository;
import server.database.EventRepository;

import java.util.*;


@RestController
@RequestMapping("/api/expense")
public class ExpenseController {
    private final ExpensesRepository repo;
    
    @Autowired
    private EventRepository eventRepository; 

    public ExpenseController(ExpensesRepository repo) {
        this.repo = repo;

    }

    @PostMapping(path = {"/addToEvent/{event_id}"})
    public ResponseEntity<Expense> add(@RequestBody Expense expense, @PathVariable("event_id") long event_id) {
        Event e = new Event(); e.setId(event_id);
        expense.setEvent(e); // I know this is cursed but truly this is the only option there is.
        Expense saved = repo.save(expense);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Expense> deleteIt(@PathVariable Long id) {
        if (id < 0 || !repo.existsById(id))
            return ResponseEntity.badRequest().build();

        repo.deleteById(id);
        List<Expense> resp = repo.findAll().stream().filter(x -> x.getId() == id).toList();
        return ResponseEntity.ok().build();
    }

    /**
    * when we need to edit an expense, the mapping needn't be altered here
    */
    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @RequestBody Expense updatedExpense) {
        Optional<Expense> expenseOptional = repo.findById(id);
        if (expenseOptional.isPresent()) {
            Expense existingExpense = expenseOptional.get();
            existingExpense.setTitle(updatedExpense.getTitle());
            existingExpense.setTotalExpense(updatedExpense.getTotalExpense());
            existingExpense.setCreator(updatedExpense.getCreator());

            Expense savedExpense = repo.save(existingExpense);
            return ResponseEntity.ok(savedExpense);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        Optional<Expense> expenseOptional = repo.findById(id);
        return expenseOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/byEvent")
    public ResponseEntity<List<Expense>> getExpensesByEvent(@RequestParam("eventId") long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event event = eventOptional.get();
        List<Expense> expenses = event.getExpenses();
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/totalExpense")
    public ResponseEntity<Double> getTotalExpenseForEvent(@RequestParam("eventId") long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event event = eventOptional.get();
        List<Expense> expenses = event.getExpenses();
        double totalExpense = expenses.stream().mapToDouble(Expense::getTotalExpense).sum();
        return ResponseEntity.ok(totalExpense);
    }

    @GetMapping("/averageExpense")
    public ResponseEntity<Double> getAverageExpenseForEvent(@RequestParam("eventId") long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event event = eventOptional.get();
        List<Expense> expenses = event.getExpenses();
        double totalExpense = expenses.stream().mapToDouble(Expense::getTotalExpense).sum();
        double averageExpense = expenses.isEmpty() ? 0 : totalExpense / expenses.size();
        return ResponseEntity.ok(averageExpense);
    }
}
