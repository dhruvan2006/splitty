package server.api;

import commons.Expense;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ExpensesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestExpenseRepository implements ExpensesRepository {
    public final List<Expense> expenses = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name) {
        calledMethods.add(name);
    }

    @Override
    public <S extends Expense> S save(S entity) {
        call("save");
        entity.setId((long) expenses.size());
        expenses.add(entity);
        return entity;
    }

    @Override
    public boolean existsById(Long id) {
        call("existsById");
        return findById(id).isPresent();
    }

    @Override
    public void deleteById(Long id) {
        call("deleteById");
        expenses.removeIf(expense -> expense.getId().equals(id));
    }

    @Override
    public Optional<Expense> findById(Long id) {
        return expenses.stream().filter(expense -> expense.getId().equals(id)).findFirst();
    }

    @Override
    public List<Expense> findAll() {
        calledMethods.add("findAll");
        return expenses;
    }


    @Override
    public void delete(Expense entity) {
        call("delete");
        expenses.remove(entity);
    }

    @Override
    public List<Expense> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Expense> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Expense> findByDescription(String description) {
        return null;
    }

    @Override
    public List<Expense> findByAmount(Double amount) {
        return null;
    }

    @Override
    public List<Expense> findByDate(String date) {
        return null;
    }

    @Override
    public List<Expense> findByCategory(String category) {
        return null;
    }

    @Override
    public <S extends Expense, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Expense> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Expense> findOne(Example<Expense> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Expense> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Expense> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Expense> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Expense> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Expense> boolean exists(Example<S> example) {
        return false;
    }
}
