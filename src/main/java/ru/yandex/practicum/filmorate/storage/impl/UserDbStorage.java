package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ModelAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Component("userDBStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "select * from USERR";
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    @Override
    public User addUser(User user) {
        List<User> users = getAllUsers();
        User checkedUser = checkName(user);
        if(users.contains(user)) {
            throw new ModelAlreadyExistException("User already exist");
        } else {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("userr")
                    .usingGeneratedKeyColumns("user_id");
            long id = insert.executeAndReturnKey(checkedUser.toMap()).longValue();
            return findUserById(id);
        }
    }

    @Override
    public User changeUSer(User user) {
        Collection<User> users = getAllUsers();
        if (users.stream().anyMatch(x -> x.getId() == user.getId())) {
            String sql = "update userr set email = ?, login = ?, user_name = ?, birth_date = ?" +
                    " where user_id = ?";
            jdbcTemplate.update(sql,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());
            return findUserById(user.getId());
        } else {
            throw new ModelNotFoundException("User not found with id " + user.getId());
        }
    }

    @Override
    public User findUserById(long id) {
        Collection<User> users = getAllUsers();
        if(users.stream().noneMatch(x -> x.getId() == id)) {
            throw new ModelNotFoundException("User not found");
        } else {
            String sql = "SELECT * FROM userr WHERE user_id = ?";
            return jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);
        }
    }

    @Override
    public List<User> getUserFriends(long id) {
        List<User> userFriends = new ArrayList<>();

        String sql = "select friend_id from friends where user_id = ?";

        List<Long> friendsId = jdbcTemplate.query(sql, this::rowMapToLongIdFriends, id);

        for (Long idLong : friendsId) {
            userFriends.add(findUserById(idLong));
        }
        return userFriends;
    }

    private long rowMapToLongIdFriends(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getLong("friend_id");
    }

    public boolean deleteUser(long id) {
        String sql = "delete from userr where user_id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    public boolean deleteAllUsers() {
        String sql = "delete from userr";
        return jdbcTemplate.update(sql) > 0;
    }

    private User checkName(User user) {
        if (user.getName().isEmpty() || user.getName() == null) {
            user.setName(user.getLogin());
        }
        return user;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("user_id");
        String email = resultSet.getString("email");
        String login = resultSet.getString("login");
        String name = resultSet.getString("user_name");
        LocalDate birthdate = resultSet.getDate("birth_date").toLocalDate();
        return new User(id, email, login, name, birthdate);
    }
}
