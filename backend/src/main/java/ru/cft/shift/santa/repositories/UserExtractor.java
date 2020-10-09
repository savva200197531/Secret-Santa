package ru.cft.shift.santa.repositories;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.cft.shift.santa.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserExtractor implements ResultSetExtractor<List<User>> {
    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<String, User> users = new HashMap<>();
        while (rs.next()) {
            String userId = rs.getString("USER_ID");
            User user;
            if (users.containsKey(userId))
                user = users.get(userId);
            else
                user = new User();
            user.setId(rs.getString("USER_ID"));
            user.setName(rs.getString("NAME"));
            user.setWishes(rs.getString("WISHES"));
            user.setRecipient(new User(
                    null,
                    rs.getString("RECIPIENT_NAME"),
                    rs.getString("RECIPIENT_WISHES"),
                    null,
                    null
            ));
            users.put(userId, user);
        }
        return new ArrayList<>(users.values());
    }
}