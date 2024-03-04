package server.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import server.database.GroupRepository;
import commons.Group;

@Controller
@RequestMapping("/api/groups")
public class GroupController {


    private GroupRepository repository;


    @GetMapping("")
    public ResponseEntity<List<Group>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping("/{id}")
    public Optional<?> findById(@PathVariable Integer id){
    Optional<Group> groupOptional = repository.findById(id);
    if(groupOptional.isPresent()) {
        return groupOptional;
    } else {
        return Optional.of("Group with ID " + id + " does not exist");
    }
}

    @PostMapping("/{id}")
    public Group updateGroup(@PathVariable Long id, @RequestBody Group updatedGroup) {
        return repository.findById(id)
                .map(group -> {
                    group.setGroupName(updatedGroup.getGroupName());
                    return repository.save(group);
                })
                .orElseThrow(() -> new RuntimeException("Group not found with id " + id));
    }

    @DeleteMapping("/groups/{id}")
    public void deleteGroup(@PathVariable Long id) {
        repository.deleteById(id);
    }
}