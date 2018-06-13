package com.gxg.dao;

import com.gxg.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by 郭欣光 on 2018/5/29.
 */

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int getUserCountByPhoneNumber(String phoneNumber) {
        String sql = "select count(*) from user where phone_number=?";
        int rowCount = this.jdbcTemplate.queryForObject(sql, Integer.class, phoneNumber);
        return rowCount;
    }

    public User getUserByPhoneNumber(String phoneNumber) {
        String sql = "select * from user where phone_number=?";
        User user = jdbcTemplate.queryForObject(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = new User();
                user.setUserId(resultSet.getString("user_id"));
                user.setPhoneNumber(resultSet.getString("phone_number"));
                user.setPassword(resultSet.getString("password"));
                user.setName(resultSet.getString("name"));
                user.setGender(resultSet.getString("gender"));
                user.setBirth(resultSet.getString("birth"));
                user.setHeadPortrait(resultSet.getString("head_portrait"));
                return user;
            }
        }, phoneNumber);
        return user;
    }

    public void insertUser(User user) {
        String sql = "insert into user values(?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getPhoneNumber(), user.getPassword(), user.getName(), user.getGender(), user.getBirth(), user.getUserId(), user.getHeadPortrait());
    }

    public void updateUser(User user) {
        String sql = "update user set phone_number=?, password=?, name=?, gender=?, birth=?, head_portrait=? where user_id=?";
        jdbcTemplate.update(sql, user.getPhoneNumber(), user.getPassword(), user.getName(), user.getGender(), user.getBirth(), user.getHeadPortrait(), user.getUserId());
    }

    public int getCount() {
        String sql = "select count(*) from user";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class);
        return rowCount;
    }

    public List<User> getAllUser() {
        String sql = "select * from user";
        List<User> userList = jdbcTemplate.query(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = new User();
                user.setUserId(resultSet.getString("user_id"));
                user.setPhoneNumber(resultSet.getString("phone_number"));
                user.setPassword(resultSet.getString("password"));
                user.setName(resultSet.getString("name"));
                user.setGender(resultSet.getString("gender"));
                user.setBirth(resultSet.getString("birth"));
                user.setHeadPortrait(resultSet.getString("head_portrait"));
                return user;
            }
        });
        return userList;
    }

    public int getCountByUserId(String userId) {
        String sql = "select count(*) from user where user_id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return rowCount;
    }

    public User getUserByUserId(String userId) {
        String sql = "select * from user where user_id=?";
        User user = jdbcTemplate.queryForObject(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = new User();
                user.setUserId(resultSet.getString("user_id"));
                user.setPhoneNumber(resultSet.getString("phone_number"));
                user.setPassword(resultSet.getString("password"));
                user.setName(resultSet.getString("name"));
                user.setGender(resultSet.getString("gender"));
                user.setBirth(resultSet.getString("birth"));
                user.setHeadPortrait(resultSet.getString("head_portrait"));
                return user;
            }
        }, userId);
        return user;
    }

    public void deleteUser(User user) {
        String sql = "delete from user where user_id=?";
        jdbcTemplate.update(sql, user.getUserId());
    }
}
