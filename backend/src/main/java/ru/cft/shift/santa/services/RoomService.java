package ru.cft.shift.santa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cft.shift.santa.exception.RoomIsFullException;
import ru.cft.shift.santa.models.Room;
import ru.cft.shift.santa.models.User;
import ru.cft.shift.santa.repositories.RoomRepository;
import ru.cft.shift.santa.repositories.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository,
                       UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public Room provideRoom(String roomId) {
        return roomRepository.fetchRoom(roomId);
    }

    public List<Room> provideAllRooms() {
        return roomRepository.getAllRooms();
    }

    public List<User> provideUsersInRoom(String roomId) {
        return userRepository.getUsersInRoom(roomId);
    }

    public Room createRoom(Room room) {
        return roomRepository.createRoom(room);
    }

    public void addUserInRoom(String roomId, User user) {
        if (!roomRepository.isRoomFull(roomId)) {
            userRepository.addUserInRoom(roomId, user);
            roomRepository.increaseRoomSize(roomId);
        } else {
            throw new RoomIsFullException();
        }
        if (roomRepository.isRoomFull(roomId)) {
            List<User> usersInRoom = provideUsersInRoom(roomId);
            Collections.shuffle(usersInRoom);
            for (int i = 0; i < usersInRoom.size(); ++i)
                userRepository.appointRecipient(
                        usersInRoom.get(i).getId(),
                        usersInRoom.get((i + 1) % usersInRoom.size()).getId()
                );
        }
    }
}