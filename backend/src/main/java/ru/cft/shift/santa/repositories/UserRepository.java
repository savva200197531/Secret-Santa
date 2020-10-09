package ru.cft.shift.santa.repositories;

import ru.cft.shift.santa.models.User;

import java.util.List;

public interface UserRepository {
    User fetchUser(String userId);
    User createUser(User user);
    List<User> getAllUsers();
    void appointRecipient(String userId, String recipientId);
    void addUserInRoom(String roomId, User user);
    List<User> getUsersInRoom(String roomId);
}