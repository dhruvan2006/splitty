package server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import server.database.GroupRepository;
import commons.Group;

@Controller
@RequestMapping("/user/groups")
public class GroupController {


    @Autowired
    private GroupRepository repository;


    @GetMapping("")
    @ResponseBody
    public List<Groups> findAll() {
        return repository.findAll();
    }

    @PostMapping("/{id}")
    public Optional<Group> findById(@PathVariable Integer id){
        return repository.findById(id).orElseThrow(() -> new ReponseStatusException(HttpStatus.NOT_FOUND,"Group does not exist"));
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