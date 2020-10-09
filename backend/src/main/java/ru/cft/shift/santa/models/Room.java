package ru.cft.shift.santa.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Room {
    private String id;
    private String name;
    private int size;
    private int capacity;
    private List<User> users;
}