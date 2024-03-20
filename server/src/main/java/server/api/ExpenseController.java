package server.api;

import commons.Expense;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.database.ExpensePayedRepository;
import server.database.ExpensesRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {
    private final ExpensesRepository repo;
    private final ExpensePayedRepository payRepo;

    public ExpenseController(ExpensesRepository repo, ExpensePayedRepository payRepo) {
        this.repo = repo;
        this.payRepo = payRepo;
    }

    /**
    * sorted by ID so the most recently created expenses show up first
    */
    @GetMapping(path = {""})
    public ResponseEntity<List<Expense>> findAllExpenses(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size,
                                                        @RequestParam(defaultValue = "id") String sortBy,
                                                        @RequestParam(defaultValue = "desc") String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Expense> expensePage = repo.findAll(pageable);
        return ResponseEntity.ok(expensePage.getContent());
    }


    @GetMapping("/{id}")
    public ResponseEntity<List<Expense>> getById(@PathVariable("id") String id) {
        long lid;
        try {
            lid = Long.parseLong(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        if (lid < 0 || !repo.existsById(lid))
            return ResponseEntity.badRequest().build();

        List<Expense> resp = repo.findAll().stream().filter(x -> x.getId() == lid).toList();
        return ResponseEntity.ok(resp);

    }

    @PostMapping(path = {"add", "/"})
    public ResponseEntity<Expense> add(@RequestBody Expense expense) {
        Expense saved = repo.save(expense);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping({"", "/"})
    public ResponseEntity<Expense> deleteIt(@RequestParam("id") Long id) {
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

        Expense savedExpense = repo.save(existingExpense);
        return ResponseEntity.ok(savedExpense);
    } else {
        return ResponseEntity.notFound().build();
    }
    }

}
