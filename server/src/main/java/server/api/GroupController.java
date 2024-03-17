package server.api;

import commons.Groups;
import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import server.database.GroupRepository;

@Controller
@RequestMapping("/api/groups")
public class GroupController {


    private GroupRepository repository;

    public GroupController(GroupRepository repository) {
        this.repository = repository;
    }

    /**
     * to be used instead of the usual findAll method, the sorting order is decreasing so you can 
     * see the most recently created groups first
     */
    @GetMapping("")
    public ResponseEntity<List<Groups>> findAll(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(defaultValue = "id") String sortBy,
                                                @RequestParam(defaultValue = "desc") String sortOrder) {
    Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
    Page<Groups> groupPage = repository.findAll(pageable);
    return ResponseEntity.ok(groupPage.getContent());
    }
    

    @PostMapping("/searchName/{id}")
    public Optional<?> findById(@PathVariable Long id){
    Optional<Groups> groupOptional = repository.findById(id);
    if(groupOptional.isPresent()) {
        return groupOptional;
    } else {
        return Optional.of("Group with ID " + id + " does not exist");
    }
    }

    @PostMapping("/{id}")
    public Groups updateGroup(@PathVariable Long id, @RequestBody Groups updatedGroup) {
        return repository.findById(id)
                .map(group -> {
                    group.setGroupName(updatedGroup.getGroupName());
                    return repository.save(group);
                })
                .orElseThrow(() -> new RuntimeException("Group not found with id " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @GetMapping("/searchName/{groupName}")
    public ResponseEntity<List<Groups>> findByGroupName(@PathVariable String groupName) {
    List<Groups> groups = repository.findByGroupName(groupName);
    if (groups.isEmpty()) {
        return ResponseEntity.notFound().build();
    }
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/searchPartial/{substring}")
    public ResponseEntity<List<Groups>> findByGroupNameContaining(@PathVariable String substring) {
        List<Groups> groups = repository.findByGroupNameContaining(substring);
        if (groups.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(groups);
    }


}