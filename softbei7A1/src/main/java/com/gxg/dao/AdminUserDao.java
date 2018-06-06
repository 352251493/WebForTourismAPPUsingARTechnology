package com.gxg.dao;

import com.gxg.entities.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by 郭欣光 on 2018/6/1.
 */

@Repository
public class AdminUserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int getCountById(String id) {
        String sql = "select count(*) from admin_user where id=?";
        int rowCount = this.jdbcTemplate.queryForObject(sql, Integer.class, id);
        return rowCount;
    }

    public AdminUser queryById(String id) {
        String sql = "select * from admin_user where id=?";
        AdminUser adminUser = jdbcTemplate.queryForObject(sql, new RowMapper<AdminUser>() {
            @Override
            public AdminUser mapRow(ResultSet resultSet, int i) throws SQLException {
                AdminUser adminUser = new AdminUser();
                adminUser.setId(resultSet.getString("id"));
                adminUser.setPassword(resultSet.getString("password"));
                return adminUser;
            }
        }, id);
        return adminUser;
    }

    public void updateAdminUser(AdminUser adminUser) {
        String sql = "update admin_user set password=? where id=?";
        jdbcTemplate.update(sql, adminUser.getPassword(), adminUser.getId());
    }
}
