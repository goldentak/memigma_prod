package kz.memigma.project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/landing")
    public String landing() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth";
    }

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }
}
