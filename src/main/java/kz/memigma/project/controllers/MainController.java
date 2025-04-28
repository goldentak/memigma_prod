package kz.memigma.project.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.Principal;

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

    @GetMapping("/api/find-people-page")
    public String serveFindPeoplePage(
            @RequestParam("token") String token,
            HttpServletResponse response) {

        Cookie jwtCookie = new Cookie("AUTH_TOKEN", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        return "find-people";
    }

    @GetMapping("/api/personal-cabinet")
    public String servePersonalCabinetPage(
            @RequestParam("token") String token,
            HttpServletResponse response) {

        Cookie jwtCookie = new Cookie("AUTH_TOKEN", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        return "personal-cabinet";
    }

    @GetMapping("/api/home")
    public String serveHomePage(
            @RequestParam("token") String token,
            HttpServletResponse response) {

        Cookie jwtCookie = new Cookie("AUTH_TOKEN", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        return "home";
    }




    @GetMapping("/api/upload")
    public String serveUploadPage(
            @RequestParam("token") String token,
            HttpServletResponse response) {

        Cookie jwtCookie = new Cookie("AUTH_TOKEN", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        return "upload";
    }

    @GetMapping("/logout")
    public String logoutPage() {
        return "redirect:/login.html";
    }

    @GetMapping("/api/go-to-cabinet")
    public String goToCabinet(Principal principal) {
        if (principal == null) {
            throw new RuntimeException("Unauthorized");
        }
        return "/personal-cabinet.html";
    }
}
