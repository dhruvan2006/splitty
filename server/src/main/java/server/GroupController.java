package server;

import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
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
    public Optional<?> findById(@PathVariable Long id){
    Optional<Group> groupOptional = repository.findById(id);
    if(groupOptional.isPresent()) {
        return groupOptional;
    } else {
        return Optional.of("Group with ID " + id + " does not exist");
    }
}

    @PutMapping("/{id}")
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