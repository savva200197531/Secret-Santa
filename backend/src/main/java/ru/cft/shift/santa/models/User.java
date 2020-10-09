package ru.cft.shift.santa.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class User {
    private String id;
    private String name;
    private String wishes;
    private User recipient;
    private Room room;
}