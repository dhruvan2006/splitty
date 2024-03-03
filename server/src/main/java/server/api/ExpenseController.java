package server.api;

import commons.Expense;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.database.ExpensesRepository;
import java.util.List;

@Controller
@RequestMapping("/api/expense")
public class ExpenseController {
    private final ExpensesRepository repo;
    ExpenseController(ExpensesRepository repo){
        this.repo = repo;
    }
    @GetMapping({"", "/"})
    public ResponseEntity<List<Expense>> getAll() {
        return ResponseEntity.ok(repo.findAll());
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
    @PostMapping({"", "/"})
    public ResponseEntity<Expense> postParticipant(@RequestBody Expense exp) {
        if (exp != null && exp.isInit())
            return ResponseEntity.badRequest().build();

        Expense saved = repo.save(exp);
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
}
