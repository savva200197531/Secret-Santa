package ru.cft.shift.santa.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cft.shift.santa.models.User;
import ru.cft.shift.santa.services.UserService;

import java.util.List;

@CrossOrigin
@RestController
public class UserController {
    private static final String USER_PATH = "/api/v001/users";
    private UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping(USER_PATH)
    public ResponseEntity<User> createUser(
            @RequestBody User user) {
        User result = service.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping(USER_PATH)
    public ResponseEntity<List<User>> readUsers() {
        List<User> result = service.provideAllUsers();
        return ResponseEntity.ok(result);
    }

    @GetMapping(USER_PATH + "/{userId}")
    public ResponseEntity<User> readUser(
            @PathVariable String userId) {
        User user = service.provideUser(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping(USER_PATH + "/randomroom")
    public ResponseEntity<User> createUserInRandomRoom(
            @RequestBody User user) {
        User result = service.createUserInRandomRoom(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}