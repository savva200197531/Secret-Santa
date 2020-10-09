package ru.cft.shift.santa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cft.shift.santa.exception.NotFoundException;
import ru.cft.shift.santa.models.Room;
import ru.cft.shift.santa.models.User;
import ru.cft.shift.santa.repositories.RoomRepository;
import ru.cft.shift.santa.repositories.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final RoomService roomService;

    @Autowired
    public UserService(UserRepository userRepository, RoomRepository roomRepository, RoomService roomService) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.roomService = roomService;
    }

    public User provideUser(String userId) {
        return userRepository.fetchUser(userId);
    }

    public List<User> provideAllUsers() {
        return userRepository.getAllUsers();
    }

    public User createUser(User user) {
        return userRepository.createUser(user);
    }

    public User createUserInRandomRoom(User user) {
        List<Room> rooms = roomRepository.getAllRooms();
        for (Room r : rooms) {
            if (r.getSize() < r.getCapacity()) {
                User returnUser = userRepository.createUser(user);
                roomService.addUserInRoom(r.getId(), returnUser);
                return returnUser;
            }
        }
        throw new NotFoundException();
    }
}