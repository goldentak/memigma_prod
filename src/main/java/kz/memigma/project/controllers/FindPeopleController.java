package kz.memigma.project.controllers;

import kz.memigma.project.services.FindPeopleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FindPeopleController {

    private final FindPeopleService findPeopleService;

    @GetMapping("/find-people")
    public Map<String, Object> findPeople(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        String token = authHeader.replace("Bearer ", "").trim();
        return findPeopleService.findSimilarUsers(token, page, size);
    }
}
