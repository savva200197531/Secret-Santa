package ru.cft.shift.santa.repositories;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.cft.shift.santa.models.Room;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RoomExtractor implements ResultSetExtractor<List<Room>> {
    @Override
    public List<Room> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<String, Room> rooms = new HashMap<>();
        while (rs.next()) {
            String roomId = rs.getString("ROOM_ID");
            Room room;
            if (rooms.containsKey(roomId))
                room = rooms.get(roomId);
            else
                room = new Room();
            room.setId(rs.getString("ROOM_ID"));
            room.setName(rs.getString("NAME"));
            room.setCapacity(rs.getInt("CAPACITY"));
            room.setSize(rs.getInt("SIZE"));
            rooms.put(roomId, room);
        }
        return new ArrayList<>(rooms.values());
    }
}