package server.api;

import commons.Expense;
import commons.ExpensePayedKey;
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
import server.database.ExpenseRepository;
import java.util.List;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {
    private final ExpenseRepository repo;
    private final ExpensePayedRepository payRepo;

    public ExpenseController(ExpenseRepository repo, ExpensePayedRepository payRepo) {
        this.repo = repo;
        this.payRepo = payRepo;
    }

    @GetMapping(path = {"", "/"})
    public List<Expense> getAll() {
        return repo.findAll();
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

    @PutMapping(path = "/pay")
    public void pay(@RequestBody ExpensePayedKey key) {
        payRepo.Pay(key.getParticipantId(), key.getExpenseId());
    }

    @DeleteMapping({"", "/"})
    public ResponseEntity<Expense> deleteIt(@RequestParam("id") Long id) {
        if (id < 0 || !repo.existsById(id))
            return ResponseEntity.badRequest().build();

        repo.deleteById(id);
        List<Expense> resp = repo.findAll().stream().filter(x -> x.getId() == id).toList();
        return ResponseEntity.ok().build();
    }
}
