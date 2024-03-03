package server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class SomeController {

    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "Welcome to the home page of team 78's project app!";
    }

/**
 * for searching another user
 */
    @PostMapping("/")
    public void receiveData(Person person) {
       System.out.println("Here is the user: " + person.getFullName());
    }
}