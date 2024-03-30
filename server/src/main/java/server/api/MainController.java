package server.api;

import commons.PasswordGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.*;
import commons.Participant;

import java.util.Random;

@Controller
@RequestMapping("/")
public class MainController {

    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "Welcome to the home page of team 78's project app!";
    }

}