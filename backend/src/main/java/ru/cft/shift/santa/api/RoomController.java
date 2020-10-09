package ru.cft.shift.santa.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cft.shift.santa.models.Room;
import ru.cft.shift.santa.models.User;
import ru.cft.shift.santa.services.RoomService;

import java.util.List;

@CrossOrigin
@RestController
public class RoomController {
    private static final String PATH = "/api/v001/rooms";
    private RoomService service;

    @Autowired
    public RoomController(RoomService service) {
        this.service = service;
    }

    @PostMapping(PATH)
    public ResponseEntity<Room> createRoom(
            @RequestBody Room room) {
        Room result = service.createRoom(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping(PATH)
    public ResponseEntity<List<Room>> readRooms() {
        List<Room> result = service.provideAllRooms();
        return ResponseEntity.ok(result);
    }

    @GetMapping(PATH + "/{roomId}/users")
    public ResponseEntity<List<User>> readUsersInRoom(
            @PathVariable String roomId) {
        List<User> users = service.provideUsersInRoom(roomId);
        return ResponseEntity.ok(users);
    }

    @GetMapping(PATH + "/{roomId}")
    public ResponseEntity<Room> readRoom(
            @PathVariable String roomId) {
        Room room = service.provideRoom(roomId);
        return ResponseEntity.ok(room);
    }

    @PostMapping(PATH + "/{roomId}/users")
    public ResponseEntity<Boolean> addUserInRoom(
            @PathVariable String roomId,
            @RequestBody User user) {
        service.addUserInRoom(roomId, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}