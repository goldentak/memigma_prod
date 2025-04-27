package kz.memigma.project.controllers;

import jakarta.servlet.http.HttpServletRequest;
import kz.memigma.project.dto.UserDto;
import kz.memigma.project.models.User;
import kz.memigma.project.services.MailSenderService;
import kz.memigma.project.services.PendingRegistrationService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import kz.memigma.project.services.JwtUtil;
import kz.memigma.project.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final PendingRegistrationService pendingService;
    private final UserService userService;
    private final MailSenderService mailSender;
    private final JwtUtil jwtUtil;

    public AuthController(PendingRegistrationService pendingService,
                          UserService userService,
                          MailSenderService mailSender,
                          JwtUtil jwtUtil) {
        this.pendingService = pendingService;
        this.userService    = userService;
        this.mailSender     = mailSender;
        this.jwtUtil        = jwtUtil;
    }

    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/auth/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET","POST","PUT","DELETE");
            }
        };
    }
    @GetMapping
    public String authPage() {
        return "auth";   // templates/auth.html
    }

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Map<String,String>> register(@RequestBody UserDto dto) {
        String code = pendingService.start(dto.getEmail(), dto.getUsername(), dto.getPassword());
        mailSender.sendHtmlEmail(dto.getEmail(), "Your registration code", code);

        String token = jwtUtil.generateToken(
                dto.getUsername(),
                Map.of("reg", true),
                Duration.ofMinutes(5)
        );

        return ResponseEntity.ok(Map.of("token", token));
    }


    @GetMapping("/verify")
    public String verifyPage() {
        return "verify";
    }

    @PostMapping(value = "/verify-code", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<?> doVerifyJson(
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = jwtUtil.extractUsername(token);
        String code = body.get("code");

        var p = pendingService.verify(username, code);
        if (p != null) {
            User u = new User();
            u.setEmail(p.email);
            u.setUsername(p.username);
            u.setPassword(p.password);
            u.setEnabled(true);
            u.setCreatedAt(Instant.now());
            userService.register(u);

            String newToken = jwtUtil.generateToken(username);
            return ResponseEntity.ok(Map.of("token", newToken));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        boolean success = userService.login(userDto.getUsername(), userDto.getPassword());
        if (success) {
            String token = jwtUtil.generateToken(userDto.getUsername());
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);

        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
        }

        String username = jwtUtil.extractUsername(token);
        return ResponseEntity.ok("Welcome, " + username + "! This is your profile.");

    }

    @PostMapping("/resend")
    @ResponseBody
    public ResponseEntity<?> resendCode(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = jwtUtil.extractUsername(token);
        PendingRegistrationService.Pending pending = pendingService.getPending(username);

        if (pending == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "No pending registration found"));
        }

        String newCode = pendingService.refreshCode(username);
        mailSender.sendHtmlEmail(pending.email, "Your new registration code", newCode);

        return ResponseEntity.ok(Map.of("message", "New code sent successfully"));
    }

    @GetMapping("/check-username")
    @ResponseBody
    public ResponseEntity<?> checkUsernameAvailability(@RequestParam String username) {
        boolean exists = userService.usernameExists(username);
        return ResponseEntity.ok().body(Map.of(
                "available", !exists
        ));
    }


    private String extractTokenFromRequest(HttpServletRequest request){
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(header == null || !header.startsWith("Bearer ")){
            return header.substring(7);
        }
        return null;
    }


    private String extractUsernameFromToken(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null && jwtUtil.validateToken(token)) {
            return jwtUtil.extractUsername(token);
        }
        return null;
    }


}
